package org.alltiny.chorus.dom;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class represents a DOM-node.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 02.01.2008 18:45:01
 */
public class Node {

    private Node parent = null;
    private Vector<Node> children = new Vector<Node>();

    public Node getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    public int getChildCount() {
        return children.size();
    }

    public Node getParent() {
        return parent;
    }

    public int getIndex(Node node) {
        return children.indexOf(node);
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    public Enumeration children() {
        return children.elements();
    }

    //////////////////
    // Mutability
    //////////////////
    private void setParent(Node newParent) {
        parent = newParent;
    }

    public void insertChild(Node node, int index) throws AlreadyBoundException {
        // break if the given node is already added as child.
        if (children.contains(node)) {
            throw new AlreadyBoundException(this, node);
        }
        // break if the given node is already attached to a parent.
        if (node.getParent() != null) {
            throw new AlreadyBoundException(this, node);
        }
        // add given node and set this node as parent.
        children.add(index, node);
        node.setParent(this);
    }

    public void attachChild(Node node) throws AlreadyBoundException {
        // break if the given node is already added as child.
        if (children.contains(node)) {
            throw new AlreadyBoundException(this, node);
        }
        // break if the given node is already attached to a parent.
        if (node.getParent() != null) {
            throw new AlreadyBoundException(this, node);
        }
        // add given node and set this node as parent.
        children.add(node);
        node.setParent(this);
    }

    public void removeChild(int index) {
        Node node = children.remove(index); // element which was removed is returned
        if (node != null) {
            node.setParent(null);
        }
    }

    public void removeChild(Node node) {
        children.remove(node);
        node.setParent(null);
    }
}
