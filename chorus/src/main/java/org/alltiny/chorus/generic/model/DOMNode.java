package org.alltiny.chorus.generic.model;

/**
 * This interface make classes listenable-to. {@link DOMMap} and {@link DOMList}
 * will register as listeners to class implementing this interface.
 */
public interface DOMNode<Self extends DOMNode> {

    DOMEventSupport domEventSupport = new DOMEventSupport();

    default Self addListener(DOMEventListener listener) {
        domEventSupport.addListener(listener);
        return (Self)this;
    }

    default Self removeListener(DOMEventListener listener) {
        domEventSupport.addListener(listener);
        return (Self)this;
    }
}
