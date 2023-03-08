package org.alltiny.chorus.model.generic;

/**
 * Is fired when a "named" property or attribute has been removed.
 * For instance a map entry has been removed from the map.
 */
public class DOMPropertyRemovedEvent<Source,Value> extends DOMPropertyChangedEvent<Source,Value> {

    public DOMPropertyRemovedEvent(Source source, DOMOperation operation, String propertyName, Value value) {
        super(source, operation, propertyName, value, null, null);
    }
}
