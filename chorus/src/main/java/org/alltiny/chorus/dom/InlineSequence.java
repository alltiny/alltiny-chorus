package org.alltiny.chorus.dom;

import org.alltiny.chorus.model.generic.DOMList;
import org.alltiny.chorus.model.generic.DOMMap;

import java.util.Collection;

/**
 * Models an inline-sequence.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008
 */
public class InlineSequence extends DOMMap<InlineSequence,Object> {

    public enum Property {
        ANCHOR_REF,
        ELEMENTS
    }

    /** This is the anchor where this inline sequence will start. not null. */
    public int getAnchorRef() {
        return (int)get(Property.ANCHOR_REF.name());
    }

    public InlineSequence setAnchorRef(int anchorRef) {
        put(Property.ANCHOR_REF.name(), anchorRef);
        return this;
    }

    public DOMList<DOMList<?,Element>,Element> getElements() {
        return (DOMList<DOMList<?,Element>,Element>)get(Property.ELEMENTS.name());
    }

    public InlineSequence setElements(Collection<Element> elements) {
        getElements().clear();
        getElements().addAll(elements);
        return this;
    }

    public InlineSequence addElement(Element element) {
        getElements().add(element);
        return this;
    }
}
