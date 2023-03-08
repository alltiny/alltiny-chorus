package org.alltiny.chorus.model.generic;

public class DOMPropertyListenerAdapter<EventSource,Value> implements DOMEventListener<EventSource> {

    private final EventSource source;
    private final String propertyName;

    public DOMPropertyListenerAdapter(EventSource source, String propertyName) {
        this.source = source;
        this.propertyName = propertyName;
    }

    @Override
    public void accept(DOMEvent<EventSource> event, Context<?> context) {
        if (event.getSource() != source) {
            return; // ignore
        }
        if (event instanceof DOMPropertyChangedEvent) {
            DOMPropertyChangedEvent changedEvent = (DOMPropertyChangedEvent)event;
            if (propertyName.equals(changedEvent.getPropertyName())) {
                changed((Value) changedEvent.getOldValue(), (Value) changedEvent.getNewValue());
            }
        }
    }

    protected void changed(Value oldValue, Value newValue) {}
}
