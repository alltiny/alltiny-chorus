package org.alltiny.chorus.dom;

import org.alltiny.chorus.base.type.AccidentalSign;
import org.alltiny.chorus.base.type.Clef;
import org.alltiny.chorus.base.type.NoteValue;
import org.alltiny.chorus.dom.decoration.Decoration;
import org.alltiny.chorus.dom.decoration.Triplet;
import org.alltiny.chorus.model.generic.DOMList;
import org.alltiny.chorus.model.generic.DOMOperation;

import java.util.Collection;

/**
 * This class represents a note.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 29.03.2007
 */
public class Note extends DurationElement<Note> {

    public enum Property {
        NOTE_VALUE,
        DECORATIONS,
        ACCIDENTAL,
        VELOCITY,
        LYRIC,
        CURRENT_CLEF // the current clef in which this note should be rendered TODO move into RenderModel
    }

    public Note() {
        put(Property.VELOCITY.name(), 64);
        put(Property.DECORATIONS.name(), new DOMList<DOMList<?,Decoration>,Decoration>());
    }

    public NoteValue getNoteValue() {
        return (NoteValue)get(Property.NOTE_VALUE.name());
    }

    public Note setNoteValue(NoteValue noteValue) {
        put(Property.NOTE_VALUE.name(), noteValue);
        return this;
    }

    public DOMList<DOMList<?,Decoration>,Decoration> getDecorations() {
        return (DOMList<DOMList<?,Decoration>,Decoration>)get(Property.DECORATIONS.name());
    }

    public Note setDecorations(Collection<Decoration> decorations) {
        getDecorations().clear();
        getDecorations().addAll(decorations);
        return this;
    }

    public Note addDecoration(Decoration decoration) {
        getDecorations().add(decoration);
        return this;
    }

    public AccidentalSign getAccidental() {
        return (AccidentalSign)get(Property.ACCIDENTAL.name());
    }

    public Note setAccidental(AccidentalSign accidental) {
        put(Property.ACCIDENTAL.name(), accidental);
        return this;
    }

    public int getVelocity() {
        return (int)get(Property.VELOCITY.name());
    }

    public Note setVelocity(int velocity) {
        put(Property.VELOCITY.name(), velocity);
        return this;
    }

    public String getLyric() {
        return (String)get(Property.LYRIC.name());
    }

    public Note setLyric(String lyric) {
        put(Property.LYRIC.name(), lyric);
        return this;
    }

    public boolean isTriplet() {
        for (Decoration deco : getDecorations()) {
            if (deco instanceof Triplet) {
                return true;
            }
        }
        return false;
    }

    public Clef getCurrentClef() {
        return (Clef)get(Property.CURRENT_CLEF.name());
    }

    public Note setCurrentClef(Clef clef) {
        put(Property.CURRENT_CLEF.name(), clef);
        return this;
    }

    public Note setCurrentClef(Clef clef, DOMOperation operation) {
        put(Property.CURRENT_CLEF.name(), clef, operation);
        return this;
    }

    @Override
    public double getLength() {
        return isTriplet() ? super.getLength() * 2 / 3 : super.getLength();
    }
}
