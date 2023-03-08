package org.alltiny.chorus.model.generic;

/**
 * Base Class for all DOM-events. The source of a DOM-event is the
 * class/item/component which fired that event.
 */
public class DOMEvent<Source> {

    private final Source source;

    private final DOMOperation operation;

    /**
     * @param operation can be null, if it is, this event is immutable. Meaning
     *     the causes can no longer be changed. The {@link DOMOperation}, if
     *     given, controls whether this event still can be changed. See
     *     {@link DOMOperation#isConcluded()}
     */

    public DOMEvent(Source source, DOMOperation operation) {
        this.source = source;
        this.operation = operation;
    }

    public Source getSource() {
        return source;
    }

    public DOMOperation getOperation() {
        return operation;
    }
}
