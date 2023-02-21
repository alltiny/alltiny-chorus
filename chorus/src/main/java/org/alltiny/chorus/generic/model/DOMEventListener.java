package org.alltiny.chorus.generic.model;

import org.alltiny.chorus.generic.model.events.DOMEvent;

import java.util.function.Consumer;

/**
 * Generic class of an event listener.
 */
public interface DOMEventListener<EventSource> extends Consumer<DOMEvent<EventSource>> {
}
