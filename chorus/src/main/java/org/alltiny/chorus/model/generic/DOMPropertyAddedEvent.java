package org.alltiny.chorus.model.generic;

/**
 * Is fired when a "named" property or attribute has been added.
 * For instance a new entry has been put into a map.
 */
public class DOMPropertyAddedEvent<Source,Value> extends DOMPropertyChangedEvent<Source,Value> {

    public DOMPropertyAddedEvent(Source source, DOMOperation operation, String propertyName, Value value) {
        super(source, operation, propertyName, null, value, null);
    }
}
