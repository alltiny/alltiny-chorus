package org.alltiny.chorus.generic.model.events;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This interface marks event that are probably caused by another event.
 * This interface can be used like an aspect.
 */
public interface DOMCausedEvent<Self> {

    AtomicReference<DOMEvent> cause = new AtomicReference<>();

    default Self withCause(DOMEvent event) {
        cause.set(event);
        return (Self)this;
    }

    default DOMEvent getCause() {
        return cause.get();
    }
}
