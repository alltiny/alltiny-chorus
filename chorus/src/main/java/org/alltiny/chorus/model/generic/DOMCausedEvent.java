package org.alltiny.chorus.model.generic;

/**
 * This interface marks event that are probably caused by another event.
 * This interface can be used like an aspect.
 */
public interface DOMCausedEvent {

    DOMEvent<?> getCause();
}
