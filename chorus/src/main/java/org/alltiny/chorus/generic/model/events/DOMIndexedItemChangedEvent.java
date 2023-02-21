package org.alltiny.chorus.generic.model.events;

/**
 * Is fired when an indexed item has been changed.
 * For instance an item in a list has been changed.
 * @param <ListType> the list type
 */
public class DOMIndexedItemChangedEvent<ListType,T> extends DOMEvent<ListType> implements DOMCausedEvent<DOMIndexedItemChangedEvent<ListType,T>> {

    private final T item;
    private final int index;
    private final T precedingItem;
    private final T followingItem;

    public DOMIndexedItemChangedEvent(ListType list, T item, int index, T precedingItem, T followingItem) {
        super(list);
        this.item = item;
        this.index = index;
        this.precedingItem = precedingItem;
        this.followingItem = followingItem;
    }

    public T getItem() {
        return item;
    }

    public int getIndex() {
        return index;
    }

    public T getPrecedingItem() {
        return precedingItem;
    }

    public T getFollowingItem() {
        return followingItem;
    }
}
