package org.alltiny.chorus.base.type;

import java.util.Objects;

/**
 * This type allows to construct a note value.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 */
public class NoteValue {

    private final BaseNote baseNote;
    private final AccidentalSign sign;
    private final int octave;

    public NoteValue(BaseNote baseNote, AccidentalSign sign, int octave) {
        this.baseNote = baseNote;
        this.sign = sign;
        this.octave = octave;
    }

    public int getMidiValue() {
        return baseNote.getValue() + sign.getValue() + 12 * octave;
    }

    public BaseNote getBaseNote() {
        return baseNote;
    }

    public int getOctave() {
        return octave;
    }

    public AccidentalSign getSign() {
        return sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoteValue noteValue = (NoteValue) o;
        return octave == noteValue.octave && baseNote == noteValue.baseNote && sign == noteValue.sign;
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseNote, sign, octave);
    }

    @Override
    public String toString() {
        return "NoteValue{" +
            "baseNote=" + baseNote +
            ", sign=" + sign +
            ", octave=" + octave +
            '}';
    }
}
