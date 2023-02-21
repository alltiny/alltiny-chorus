package org.alltiny.chorus.generic.model;

import org.alltiny.chorus.generic.model.events.DOMEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper class to hold multiple listeners and fire events.
 */
public class DOMEventSupport {

    private final List<DOMEventListener> listeners = new ArrayList<>();

    public void addListener(DOMEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DOMEventListener listener) {
        listeners.remove(listener);
    }

    public void fireEvent(DOMEvent event) {
        listeners.forEach(l -> l.accept(event));
    }

    public void fireEvents(Collection<? extends DOMEvent> events) {
        events.forEach(e -> fireEvent(e));
    }
}
