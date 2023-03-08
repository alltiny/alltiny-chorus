package org.alltiny.chorus.dom;

import org.alltiny.chorus.model.generic.DOMList;
import org.alltiny.chorus.model.generic.DOMMap;

import java.util.Collection;

/**
 * This class represents a single voice which is a sequence of notes and rests.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.12.2007 20:31:08
 */
public class Voice extends DOMMap<Voice,Object> {

    public enum Property {
        HEAD,
        NAME,
        MAIN_SEQUENCE,
        INLINE_SEQUENCES,
        MUTED
    }

    public Voice() {
        put(Property.INLINE_SEQUENCES.name(), new DOMList<DOMList<?,InlineSequence>,InlineSequence>());
    }

    public String getHead() {
        return (String)get(Property.HEAD.name());
    }

    public Voice setHead(String head) {
        put(Property.HEAD.name(), head);
        return this;
    }

    public String getName() {
        return (String)get(Property.NAME.name());
    }

    public Voice setName(String name) {
        put(Property.NAME.name(), name);
        return this;
    }

    public Sequence getSequence() {
        return (Sequence)get(Property.MAIN_SEQUENCE.name());
    }

    public Voice setSequence(Sequence sequence) {
        put(Property.MAIN_SEQUENCE.name(), sequence);
        return this;
    }

    public DOMList<DOMList<?,InlineSequence>,InlineSequence> getInlineSequences() {
        return (DOMList<DOMList<?,InlineSequence>,InlineSequence>)get(Property.INLINE_SEQUENCES.name());
    }

    public Voice setInlineSequences(Collection<InlineSequence> inlineSequences) {
        getInlineSequences().clear();
        getInlineSequences().addAll(inlineSequences);
        return this;
    }

    public Voice addInlineSequence(InlineSequence sequence) {
        getInlineSequences().add(sequence);
        return this;
    }

    public Boolean getMuted() {
        return (Boolean)get(Property.MUTED.name());
    }

    public Voice setMuted(Boolean muted) {
        put(Property.MUTED.name(), muted);
        return this;
    }
}
