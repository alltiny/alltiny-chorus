package org.alltiny.chorus.model.generic;

public class Context<Identifier> {

    private final Identifier identifier;
    private final DOMNode<?> node;
    private final DOMOperation operation;
    private final Context<?> parent;

    public Context(Identifier identifier, DOMNode<?> node, DOMOperation operation, Context<?> parent) {
        this.identifier = identifier;
        this.node = node;
        this.operation = operation;
        this.parent = parent;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public DOMNode<?> getNode() {
        return node;
    }

    public DOMOperation getOperation() {
        return operation;
    }

    public Context<?> getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "Context{'" + identifier + "':" + parent + '}';
    }
}
