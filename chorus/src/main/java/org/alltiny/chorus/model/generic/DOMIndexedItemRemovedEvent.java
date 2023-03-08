package org.alltiny.chorus.model.generic;

/**
 * Is fired when an indexed item has been removed.
 * For instance an item has been removed from a list.
 * @param <ListType> the list type
 */
public class DOMIndexedItemRemovedEvent<ListType,T> extends DOMIndexedItemChangedEvent<ListType,T> {

    public DOMIndexedItemRemovedEvent(ListType list, DOMOperation operation, T item, int index, DOMEvent<?> cause) {
        super(list, operation, item, index, cause);
    }
}
