package org.alltiny.chorus.generic.model;

import org.alltiny.chorus.generic.model.events.DOMListClearedEvent;
import org.alltiny.chorus.generic.model.events.DOMIndexedItemChangedEvent;
import org.alltiny.chorus.generic.model.events.DOMIndexedItemInsertedEvent;
import org.alltiny.chorus.generic.model.events.DOMIndexedItemRemovedEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Implementation of a list firing {@link org.alltiny.chorus.generic.model.events.DOMEvent}s.
 */
public class DOMList<Self extends DOMList,T> implements DOMNode<Self>, List<T> {

    private final List<T> elements = new ArrayList<>();

    private final DOMEventListener<T> relayListener = (event) -> {
        final T item = event.getSource();
        final int index = elements.indexOf(item);
        domEventSupport.fireEvent(new DOMIndexedItemChangedEvent<>(
            (Self) this,
            item,
            index,
            (index > 1) ? elements.get(index - 1) : null,
            (index < elements.size() - 2) ? elements.get(index + 1) : null)
            .withCause(event)
        );
    };

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
        if (elements.add(t)) {
            final int index = elements.size();

            if (t instanceof DOMNode) {
                ((DOMNode)t).addListener(relayListener);
            }

            domEventSupport.fireEvent(new DOMIndexedItemInsertedEvent<>(this,
                t,
                index,
                (index > 1) ? elements.get(index - 1) : null,
                null));
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        final int index = elements.indexOf(o);

        final T preceding = (index > 1) ? elements.get(index - 1) : null;
        final T following = (index < elements.size() - 2) ? elements.get(index + 1) : null;

        if (elements.remove(o)) {
            if (o instanceof DOMNode) {
                ((DOMNode)o).removeListener(relayListener);
            }

            domEventSupport.fireEvent(new DOMIndexedItemRemovedEvent<>(this, o, index, preceding, following));
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
        // prepare all events
        DOMListClearedEvent<DOMList<Self,T>,T> clearedEvent = new DOMListClearedEvent<>(this);
        // the remove events a fired for listeners not implementing the cleared event.
        List<DOMIndexedItemRemovedEvent<DOMList<Self,T>,T>> removedEvents = elements.stream()
                .map(item ->
                    new DOMIndexedItemRemovedEvent<>(
                        this,
                        item,
                        0,
                        null,
                        null)
                    .withCause(clearedEvent) /* this may give listeners a chance to ignore these
                     events when the cleared-event has already been handled. */
                )
                .collect(Collectors.toList());

        elements.clear();

        // fire all events
        domEventSupport.fireEvent(clearedEvent);
        domEventSupport.fireEvents(removedEvents);
    }

    @Override
    public T get(int index) {
        return elements.get(index);
    }

    @Override
    public T set(int index, T element) {
        final T preceding = (index > 1) ? elements.get(index - 1) : null;
        final T following = (index < elements.size() - 2) ? elements.get(index + 1) : null;

        final T removed = elements.set(index, element);

        if (element instanceof DOMNode) {
            ((DOMNode)element).addListener(relayListener);
        }
        if (removed instanceof DOMNode) {
            ((DOMNode)removed).removeListener(relayListener);
        }

        domEventSupport.fireEvent(new DOMIndexedItemRemovedEvent<>(this, removed, index, preceding, following));
        domEventSupport.fireEvent(new DOMIndexedItemInsertedEvent<>(this, element, index, preceding, following));
        return removed;
    }

    @Override
    public void add(int index, T element) {
        elements.add(index, element);

        if (element instanceof DOMNode) {
            ((DOMNode)element).addListener(relayListener);
        }

        domEventSupport.fireEvent(new DOMIndexedItemInsertedEvent<>(
            this, element, index,
            (index > 1) ? elements.get(index - 1) : null,
            (index < elements.size() - 2) ? elements.get(index + 1) : null
        ));
    }

    @Override
    public T remove(int index) {
        final T preceding = (index > 1) ? elements.get(index - 1) : null;
        final T following = (index < elements.size() - 2) ? elements.get(index + 1) : null;

        T removed = elements.remove(index);

        if (removed instanceof DOMNode) {
            ((DOMNode)removed).removeListener(relayListener);
        }

        if (removed != null) {
            domEventSupport.fireEvent(new DOMIndexedItemRemovedEvent<>(this, removed, index, preceding, following));
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
}
