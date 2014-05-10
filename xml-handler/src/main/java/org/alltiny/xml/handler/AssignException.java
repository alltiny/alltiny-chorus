package org.alltiny.xml.handler;

/**
 * This exception is thrown to signalize that the given {@link #child} could not
 * be assigned to the given {@link #parent}. The text in {@link #reason} may tell
 * why.
 */
public class AssignException extends Exception {

    private final Object parent;
    private final Object child;
    private final String reason;

    /**
     * Creates a new exception telling that the given {@param child} could
     * be assign to the given {@param parent}.
     *
     * @param child which could not be assigned to the parent.
     * @param parent which did not accept the given child.
     * @param reason should tell why parent did not accept child.
     */
    public AssignException(Object parent, Object child, String reason) {
        this.parent = parent;
        this.child = child;
        this.reason = reason;
    }

    public Object getParent() {
        return parent;
    }

    public Object getChild() {
        return child;
    }

    public String getReason() {
        return reason;
    }

    public String toString() {
        return "Could not assign \'" + child + "\' to \'" + parent + "\': " + reason;
    }
}
