package org.alltiny.chorus.generic.model;

import org.alltiny.chorus.generic.model.events.DOMPropertyAddedEvent;
import org.alltiny.chorus.generic.model.events.DOMPropertyChangedEvent;
import org.alltiny.chorus.generic.model.events.DOMPropertyRemovedEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of a map firing {@link org.alltiny.chorus.generic.model.events.DOMEvent}s.
 */
public class DOMMap<Self extends DOMMap,Value> implements DOMNode<Self>, Map<String,Value> {

    private final Map<String,PropertyHolder<Value>> properties = new HashMap<>();

    /** This relayListener tries to hide use of the PropertyHolder by rewriting the
     * PropertyChangeEvent. */
    private final DOMEventListener<PropertyHolder<Value>> relayListener = (event) -> {
        if (event instanceof DOMPropertyChangedEvent) {
            final DOMPropertyChangedEvent<PropertyHolder<Value>,Value> pce = ((DOMPropertyChangedEvent)event);
            domEventSupport.fireEvent(new DOMPropertyChangedEvent<>(
                (Self) this,
                pce.getPropertyName(),
                pce.getOldValue(),
                pce.getNewValue())
                .withCause(pce.getCause())
            );
        } else {
            domEventSupport.fireEvent(event);
        }
    };

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
        PropertyHolder<Value> ph = properties.get(key);
        if (ph == null) {
            ph = new PropertyHolder<>(key);
            ph.setValue(value);
            ph.addListener(relayListener);

            domEventSupport.fireEvent(new DOMPropertyAddedEvent<>(this, key, value));

            return null;
        } else {
            return ph.setValue(value);
        }
    }

    @Override
    public Value remove(Object key) {
        PropertyHolder<Value> ph = properties.remove(key);
        if (ph != null) {
            ph.removeListener(relayListener);

            domEventSupport.fireEvent(new DOMPropertyRemovedEvent<>(this, ph.getName(), ph.getValue()));

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
        throw new UnsupportedOperationException();
    }

    /**
     * This class helps in firing {@link DOMPropertyChangedEvent}s which should contain
     * the property's name. This holder spares us to create a reverse look-up-map.
     */
    private static class PropertyHolder<Value> implements DOMNode<PropertyHolder<Value>> {

        private final DOMEventListener<Value> relayListener;
        private final String name;

        private Value value;

        public PropertyHolder(String name) {
            this.name = name;
            this.relayListener = (event) -> {
                final Value item = event.getSource();
                domEventSupport.fireEvent(new DOMPropertyChangedEvent<>(
                    this,
                    name,
                    item,
                    item)
                    .withCause(event)
                );
            };
        }

        public String getName() {
            return name;
        }

        public Value getValue() {
            return value;
        }

        public Value setValue(Value value) {
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
                    name,
                    oldValue,
                    value
                    ));
            }
            return oldValue;
        }
    }
}
