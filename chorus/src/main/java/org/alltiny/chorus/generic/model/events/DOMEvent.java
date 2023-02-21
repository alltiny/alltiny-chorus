package org.alltiny.chorus.generic.model.events;

/**
 * Base Class for all DOM-events. The source of a DOM-event is the
 * class/item/component which fired that event.
 */
public class DOMEvent<Source> {

    private final Source source;

    public DOMEvent(Source source) {
        this.source = source;
    }

    public Source getSource() {
        return source;
    }
}
