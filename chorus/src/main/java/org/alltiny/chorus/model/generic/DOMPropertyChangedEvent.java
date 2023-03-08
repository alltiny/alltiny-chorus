package org.alltiny.chorus.model.generic;

/**
 * Is fired when a "named" property or attribute has been changed.
 * For instance a map entry has been changed, or an attribute of a class
 * has been changed.
 */
public class DOMPropertyChangedEvent<Source,Value> extends DOMEvent<Source> implements DOMCausedEvent {

    private final String propertyName;
    private final Value oldValue;
    private final Value newValue;
    private final DOMEvent<?> cause;

    public DOMPropertyChangedEvent(Source source, DOMOperation operation, String propertyName, Value oldValue, Value newValue, DOMEvent<?> cause) {
        super(source, operation);
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.cause = cause;
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

    @Override
    public DOMEvent<?> getCause() {
        return cause;
    }
}
