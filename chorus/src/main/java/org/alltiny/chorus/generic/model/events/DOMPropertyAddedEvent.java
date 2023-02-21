package org.alltiny.chorus.generic.model.events;

/**
 * Is fired when a "named" property or attribute has been added.
 * For instance a new entry has been put into a map.
 */
public class DOMPropertyAddedEvent<Source,Value> extends DOMEvent<Source> {

    private final String propertyName;
    private final Value value;

    public DOMPropertyAddedEvent(Source source, String attributeName, Value value) {
        super(source);
        this.propertyName = attributeName;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Value getValue() {
        return value;
    }
}
