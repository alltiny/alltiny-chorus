package org.alltiny.chorus.generic.model.events;

/**
 * Is fired when a "named" property or attribute has been removed.
 * For instance a map entry has been removed from the map.
 */
public class DOMPropertyRemovedEvent<Source,Value> extends DOMEvent<Source> {

    private final String propertyName;
    private final Value value;

    public DOMPropertyRemovedEvent(Source source, String attributeName, Value value) {
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
