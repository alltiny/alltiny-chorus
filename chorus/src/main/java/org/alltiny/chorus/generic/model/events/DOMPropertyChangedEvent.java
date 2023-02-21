package org.alltiny.chorus.generic.model.events;

/**
 * Is fired when a "named" property or attribute has been changed.
 * For instance a map entry has been changed, or an attribute of a class
 * has been changed.
 */
public class DOMPropertyChangedEvent<Source,Value> extends DOMEvent<Source> implements DOMCausedEvent<DOMPropertyChangedEvent<Source,Value>> {

    private final String propertyName;
    private final Value oldValue;
    private final Value newValue;

    public DOMPropertyChangedEvent(Source source, String attributeName, Value oldValue, Value newValue) {
        super(source);
        this.propertyName = attributeName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Value getOldValue() {
        return oldValue;
    }

    public Value getNewValue() {
        return newValue;
    }
}
