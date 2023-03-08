package org.alltiny.chorus.model.generic;

/**
 * This event signals the end of a DOM-operation. A "DOM-operation" is any action
 * which performs multiple changes in the DOM and thus would create as many change
 * events for each single manipulation. "DOM-operation" allow to tie multiple
 * change events together and release them with this {@link DOMOperationFinished}
 * event. Be aware that {@link #operationId} should be globally unique in the time
 * span from its creation until this event is processed.
 */
public class DOMOperationFinished<Source> {

    private final Source source;
    private final String operationId;

    public DOMOperationFinished(Source source, String operationId) {
        this.source = source;
        this.operationId = operationId;
    }

    public Source getSource() {
        return source;
    }

    public String getOperationId() {
        return operationId;
    }
}
