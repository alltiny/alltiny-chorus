package org.alltiny.chorus.dom;

import org.alltiny.chorus.base.type.Clef;
import org.alltiny.chorus.base.type.Key;
import org.alltiny.chorus.model.generic.DOMList;
import org.alltiny.chorus.model.generic.DOMMap;

import java.util.Collection;

/**
 * Models a main-sequence of a a {@link Voice}.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008
 */
public class Sequence extends DOMMap<Sequence,Object> {

    public enum Property {
        CLEF,
        KEY,
        ELEMENTS
    }

    public Sequence() {
        put(Property.ELEMENTS.name(), new DOMList<DOMList<?,Element<?>>,Element<?>>());
    }

    public Clef getClef() {
        return (Clef)get(Property.CLEF.name());
    }

    public Sequence setClef(Clef clef) {
        put(Property.CLEF.name(), clef);
        return this;
    }

    public Key getKey() {
        return (Key)get(Property.KEY.name());
    }

    public Sequence setKey(Key key) {
        put(Property.KEY.name(), key);
        return this;
    }

    public DOMList<DOMList<?,Element<?>>,Element<?>> getElements() {
        return (DOMList<DOMList<?,Element<?>>,Element<?>>)get(Property.ELEMENTS.name());
    }

    public Sequence setElements(Collection<Element<?>> elements) {
        getElements().clear();
        getElements().addAll(elements);
        return this;
    }

    public Sequence addElement(Element<?> element) {
        getElements().add(element);
        return this;
    }
}
