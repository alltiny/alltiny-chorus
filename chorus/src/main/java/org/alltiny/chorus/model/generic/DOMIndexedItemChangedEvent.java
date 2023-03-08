package org.alltiny.chorus.model.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * Is fired when an indexed item has been changed.
 * For instance an item in a list has been changed.
 * @param <ListType> the list type
 */
public class DOMIndexedItemChangedEvent<ListType,T> extends DOMEvent<ListType> implements DOMCausedEvent {

    private final T item;
    private final int index;
    private final DOMEvent<?> cause;

    public DOMIndexedItemChangedEvent(ListType list, DOMOperation operation, T item, int index, DOMEvent<?> cause) {
        super(list, operation);
        this.item = item;
        this.index = index;
        this.cause = cause;
    }

    public T getItem() {
        return item;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public DOMEvent<?> getCause() {
        return cause;
    }
}
