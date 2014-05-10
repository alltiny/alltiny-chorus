package org.alltiny.chorus.dom;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 02.01.2008 19:27:03
 */
public class AlreadyBoundException extends Exception {

    private final Node parent;
    private final Node child;

    public AlreadyBoundException(Node parent, Node child) {
        this.parent = parent;
        this.child  = child;
    }

    public Node getParent() {
        return parent;
    }

    public Node getChild() {
        return child;
    }

    public String toString() {
        return "Node " + child + " is already bound to node " + parent;
    }
}
