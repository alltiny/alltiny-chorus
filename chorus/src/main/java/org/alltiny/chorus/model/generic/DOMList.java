package org.alltiny.chorus.model.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Implementation of a list firing {@link DOMEvent}s.
 */
public class DOMList<Self extends DOMList<?,T>,T> implements DOMNode<Self>, List<T> {

    private final DOMEventSupport domEventSupport = new DOMEventSupport();

    private final List<T> elements = new ArrayList<>();

    private final DOMEventListener<T> relayListener = (event,context) -> {
        final T item = event.getSource();
        final int index = elements.indexOf(item);
        domEventSupport.fireEvent(new DOMIndexedItemChangedEvent<>(
            (Self) this,
            event.getOperation(),
            item,
            index,
            event
        ));
    };

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
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return elements.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return add(t, null);
    }

    public boolean add(T t, DOMOperation operation) {
        if (elements.add(t)) {
            final int index = elements.size() - 1;

            if (t instanceof DOMNode) {
                ((DOMNode)t).addListener(relayListener);
            }

            domEventSupport.fireEvent(new DOMIndexedItemInsertedEvent<>(this, operation, t, index));
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return remove(o, null);
    }

    public boolean remove(Object o, DOMOperation operation) {
        final int index = elements.indexOf(o);

        if (elements.remove(o)) {
            if (o instanceof DOMNode) {
                ((DOMNode)o).removeListener(relayListener);
            }

            domEventSupport.fireEvent(new DOMIndexedItemRemovedEvent<>(this, operation, o, index, null));
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return elements.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        final int size = elements.size();
        c.forEach(this::add);
        return size != elements.size();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        int offset = 0;
        for (T o : c) {
            final int size = size();
            add(index + offset, o);
            if (size < size()) {
                offset++;
            }
        }
        return offset > 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        final int size = size();
        c.forEach(this::remove);
        return size != size();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        final List<T> itemsToRemove = elements.stream()
            .filter(c::contains)
            .collect(Collectors.toList());
        return removeAll(itemsToRemove);
    }

    @Override
    public void clear() {
        final DOMOperation operation = new DOMOperation("clearing list");
        // prepare all events
        DOMListClearedEvent<DOMList<Self,T>,T> clearedEvent = new DOMListClearedEvent<>(this, operation);
        // the remove events a fired for listeners not implementing the cleared event.
        List<DOMIndexedItemRemovedEvent<DOMList<Self,T>,T>> removedEvents = elements.stream()
                .map(item ->
                    new DOMIndexedItemRemovedEvent<>(
                        this,
                        operation,
                        item,
                        0,
                        clearedEvent) /* this may give listeners a chance to ignore these
                     events when the cleared-event has already been handled. */
                )
                .collect(Collectors.toList());

        elements.clear();

        // fire all events
        domEventSupport.fireEvent(clearedEvent);
        domEventSupport.fireEvents(removedEvents);
        operation.conclude();
    }

    @Override
    public T get(int index) {
        return elements.get(index);
    }

    @Override
    public T set(int index, T element) {
        return set(index, element, null);
    }

    public T set(int index, T element, DOMOperation operation) {
        final DOMOperation op = (operation != null) ? operation : new DOMOperation("replacing list item");

        final T removed = elements.set(index, element);

        if (element instanceof DOMNode) {
            ((DOMNode)element).addListener(relayListener);
        }
        if (removed instanceof DOMNode) {
            ((DOMNode)removed).removeListener(relayListener);
        }

        domEventSupport.fireEvent(new DOMIndexedItemRemovedEvent<>(this, op, removed, index, null));
        domEventSupport.fireEvent(new DOMIndexedItemInsertedEvent<>(this, op, element, index));
        return removed;
    }

    @Override
    public void add(int index, T element) {
        add(index, element, null);
    }

    public void add(int index, T element, DOMOperation operation) {
        elements.add(index, element);

        if (element instanceof DOMNode) {
            ((DOMNode)element).addListener(relayListener);
        }

        domEventSupport.fireEvent(new DOMIndexedItemInsertedEvent<>(this, operation, element, index));
    }

    @Override
    public T remove(int index) {
        return remove(index, null);
    }

    public T remove(int index, DOMOperation operation) {
        T removed = elements.remove(index);

        if (removed instanceof DOMNode) {
            ((DOMNode)removed).removeListener(relayListener);
        }

        if (removed != null) {
            domEventSupport.fireEvent(new DOMIndexedItemRemovedEvent<>(this, operation, removed, index, null));
        }
        return removed;
    }

    @Override
    public int indexOf(Object o) {
        return elements.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return elements.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return elements.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return elements.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return elements.subList(fromIndex, toIndex);
    }

    /** This helper method ensures the index is in bounds */
    public int ensureIndex(int index) {
        return Math.max(0, Math.min(size(), index));
    }
}
