package org.alltiny.chorus.dom;

import org.alltiny.chorus.model.generic.DOMMap;

/**
 * Models an element of a sequence.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008
 */
public class Element<Self extends Element<Self>> extends DOMMap<Self,Object> {

    public enum Property {
        SEQUENCE
    }

    @Deprecated
    public Sequence getSequence() {
        return (Sequence)get(Property.SEQUENCE.name());
    }

    @Deprecated
    public Self setSequence(Sequence sequence) {
        put(Property.SEQUENCE.name(), sequence);
        return (Self)this;
    }
}
