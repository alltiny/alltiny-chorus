package org.alltiny.chorus.model.generic;

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

    public void fireEvent(DOMEvent event, Context<?> context) {
        listeners.forEach(l -> {
            long startTimestamp = System.currentTimeMillis();
            try {
                l.accept(event, context);
            } catch (Exception e) {
                System.out.println("exception escaped from event listener " + l + ": " + e);
            }
            long delta = System.currentTimeMillis() - startTimestamp;
            if (delta > 20) {
                System.out.println("listener " + l + " took " + delta + "ms to complete");
            }
        });
    }

    public void fireEvent(DOMEvent event) {
        fireEvent(event, null);
    }

    public void fireEvents(Collection<? extends DOMEvent> events) {
        events.forEach(e -> fireEvent(e));
    }
}
