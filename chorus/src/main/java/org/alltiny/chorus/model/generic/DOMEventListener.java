package org.alltiny.chorus.model.generic;

import java.util.function.BiConsumer;

/**
 * Generic class of an event listener.
 */
public interface DOMEventListener<EventSource> extends BiConsumer<DOMEvent<EventSource>,Context<?>> {

    /**
     * This method will be called by the model after this lister was registered
     * to it as a listener. The purpose is to give the listener a chance to updated
     * itself when registering to a non-empty model.
     */
    default void initialize(EventSource model, Context<?> context) {}

    /**
     * This method will be called by the model when unregistering a listener,
     * or on model-destruction to inform all registered listeners that the values
     * vanish.
     */
    default void shutdown(EventSource model, Context<?> context) {}
}
