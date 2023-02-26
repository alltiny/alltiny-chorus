package org.alltiny.chorus.generic.model;

/**
 * This interface make classes listenable-to. {@link DOMMap} and {@link DOMList}
 * will register as listeners to class implementing this interface.
 */
public interface DOMNode<Self extends DOMNode> {

    Self addListener(DOMEventListener<Self> listener);

    Self removeListener(DOMEventListener<Self> listener);
}
