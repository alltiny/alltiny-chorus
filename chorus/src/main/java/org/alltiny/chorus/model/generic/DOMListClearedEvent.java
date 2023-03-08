package org.alltiny.chorus.model.generic;

/**
 * Is fired when a list has been cleared.
 * @param <ListType> the list type
 */
public class DOMListClearedEvent<ListType,T> extends DOMEvent<ListType> {

    public DOMListClearedEvent(ListType list, DOMOperation operation) {
        super(list, operation);
    }
}
