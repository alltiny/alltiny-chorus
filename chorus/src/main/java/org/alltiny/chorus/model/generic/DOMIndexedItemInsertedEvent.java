package org.alltiny.chorus.model.generic;

/**
 * Is fired when an indexed item has been inserted.
 * For instance an item in a list has added at the end or in between existing items.
 * @param <ListType> the list type
 */
public class DOMIndexedItemInsertedEvent<ListType,T> extends DOMIndexedItemChangedEvent<ListType,T> {

    public DOMIndexedItemInsertedEvent(ListType list, DOMOperation operation, T item, int index) {
        super(list, operation, item, index, null);
    }
}
