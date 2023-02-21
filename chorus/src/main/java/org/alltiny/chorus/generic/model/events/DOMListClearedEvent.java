package org.alltiny.chorus.generic.model.events;

/**
 * Is fired when a list has been cleared.
 * @param <ListType> the list type
 */
public class DOMListClearedEvent<ListType,T> extends DOMEvent<ListType> {

    public DOMListClearedEvent(ListType list) {
        super(list);
    }
}
