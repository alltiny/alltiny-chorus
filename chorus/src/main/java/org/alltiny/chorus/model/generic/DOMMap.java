package org.alltiny.chorus.model.generic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of a map firing {@link DOMEvent}s.
 */
public class DOMMap<Self extends DOMMap<?,?>,Value> implements DOMNode<Self>, Map<String,Value> {

    private final DOMEventSupport domEventSupport = new DOMEventSupport();

    private final Map<String,PropertyHolder<Value>> properties = new HashMap<>();

    /** This relayListener tries to hide use of the PropertyHolder by rewriting the
     * PropertyChangeEvent. */
    private final DOMEventListener<PropertyHolder<Value>> relayListener = (event,context) -> {
        if (event instanceof DOMPropertyChangedEvent) {
            final DOMPropertyChangedEvent<PropertyHolder<Value>,Value> pce = ((DOMPropertyChangedEvent)event);
            domEventSupport.fireEvent(new DOMPropertyChangedEvent<>(
                (Self) this,
                pce.getOperation(),
                pce.getPropertyName(),
                pce.getOldValue(),
                pce.getNewValue(),
                pce.getCause()
            ));
        }
    };

    public DOMMap() {}

    public DOMMap(Map<String,Value> map) {
        putAll(map);
    }

    @Override
    public Self addListener(DOMEventListener<Self> listener) {
        domEventSupport.addListener(listener);
        listener.initialize((Self)this, null);
        return (Self)this;
    }

    @Override
    public Self removeListener(DOMEventListener<Self> listener) {
        domEventSupport.removeListener(listener);
        listener.shutdown((Self)this, null);
        return (Self)this;
    }

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value get(Object key) {
        PropertyHolder<Value> ph = properties.get(key);
        return (ph != null) ? ph.getValue() : null;
    }

    @Override
    public Value put(String key, Value value) {
        final DOMOperation operation = new DOMOperation("replacing " + key);
        final Value oldValue = put(key, value, operation);
        operation.conclude();
        return oldValue;
    }

    public Value put(String key, Value value, DOMOperation operation) {
        PropertyHolder<Value> ph = properties.get(key);
        if (ph == null) {
            ph = new PropertyHolder<>(key);
            ph.setValue(value, operation);
            ph.addListener(relayListener);
            properties.put(key, ph);

            domEventSupport.fireEvent(new DOMPropertyAddedEvent<>(this, operation, key, value));

            return null;
        } else {
            return ph.setValue(value, operation);
        }
    }

    @Override
    public Value remove(Object key) {
        return remove(key, null);
    }

    public Value remove(Object key, DOMOperation operation) {
        PropertyHolder<Value> ph = properties.remove(key);
        if (ph != null) {
            ph.removeListener(relayListener);

            domEventSupport.fireEvent(new DOMPropertyRemovedEvent<>(this, operation, ph.getName(), ph.getValue()));

            return ph.getValue();
        } else {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends String,? extends Value> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        properties.forEach((name,ph) -> properties.remove(name));
    }

    @Override
    public Set<String> keySet() {
        return properties.keySet();
    }

    @Override
    public Collection<Value> values() {
        return properties.values().stream()
            .map(PropertyHolder::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public Set<Entry<String,Value>> entrySet() {
        return properties.entrySet().stream()
            .map(pe -> new DOMMapEntry<>(pe.getKey(), pe.getValue().getValue()))
            .collect(Collectors.toSet());
    }

    /**
     * This class helps in firing {@link DOMPropertyChangedEvent}s which should contain
     * the property's name. This holder spares us to create a reverse look-up-map.
     */
    private static class PropertyHolder<Value> implements DOMNode<PropertyHolder<Value>> {

        private final DOMEventSupport domEventSupport = new DOMEventSupport();
        private final DOMEventListener<Value> relayListener;
        private final String name;

        private Value value;

        public PropertyHolder(String name) {
            this.name = name;
            this.relayListener = (event,context) -> {
                final Value item = event.getSource();
                domEventSupport.fireEvent(new DOMPropertyChangedEvent<>(
                    this,
                    event.getOperation(),
                    name,
                    item,
                    item,
                    event
                ));
            };
        }

        @Override
        public PropertyHolder<Value> addListener(DOMEventListener listener) {
            domEventSupport.addListener(listener);
            return this;
        }

        @Override
        public PropertyHolder<Value> removeListener(DOMEventListener listener) {
            domEventSupport.removeListener(listener);
            return this;
        }


        public String getName() {
            return name;
        }

        public Value getValue() {
            return value;
        }

        public Value setValue(Value value, DOMOperation operation) {
            final Value oldValue = this.value;
            this.value = value;
            if (!Objects.equals(value, oldValue)) {
                if (oldValue instanceof DOMNode) {
                    ((DOMNode)oldValue).removeListener(relayListener);
                }
                if (value instanceof DOMNode) {
                    ((DOMNode)value).addListener(relayListener);
                }
                domEventSupport.fireEvent(new DOMPropertyChangedEvent<>(
                    this,
                    operation,
                    name,
                    oldValue,
                    value,
                    null));
            }
            return oldValue;
        }
    }

    private static class DOMMapEntry<K,V> implements Map.Entry<K,V> {

        private final K key;
        private final V value;

        public DOMMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }
}
