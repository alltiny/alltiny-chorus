package org.alltiny.chorus.dom;

import org.alltiny.chorus.base.type.AccidentalSign;
import org.alltiny.chorus.base.type.BaseNote;
import org.alltiny.chorus.dom.decoration.Decoration;
import org.alltiny.chorus.dom.decoration.Accidental;
import org.alltiny.chorus.dom.decoration.Triplet;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a note.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 29.03.2007
 */
public class Note extends DurationElement {

    private BaseNote note;
    private AccidentalSign sign;
    private int octave;
    private List<Decoration> decoration = new ArrayList<Decoration>();
    private Accidental accidental;
    private int velocity;
    private String lyric;

    /**
     * This constructor creates a Note object with the given value and duration.
     *
     * @param note the base note
     * @param duration of this note. <code>1.0</code> for one time unit; <code>0.5</code> for a half unit.
     * */
    public Note(BaseNote note, AccidentalSign sign, int octave, int duration, int division) {
        super(duration, division);
        this.note = note;
        this.sign = sign;
        this.octave = octave;
        velocity = 64;
    }

    public void addDecoration(Decoration decoration) {
        this.decoration.add(decoration);
    }

    public List<Decoration> getDecorations() {
        return decoration;
    }

    public int getMidiValue() {
        return note.getValue() + sign.getValue() + 12 * octave;
    }

    public Accidental getAccidental() {
        return accidental;
    }

    public void setAccidental(Accidental accidental) {
        this.accidental = accidental;
    }

    public int getVeloXcity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public BaseNote getNote() {
        return note;
    }

    public int getOctave() {
        return octave;
    }

    public AccidentalSign getSign() {
        return sign;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public boolean isTriplet() {
        for (Decoration deco : decoration) {
            if (deco instanceof Triplet) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getLength() {
        return isTriplet() ? super.getLength() * 2 / 3 : super.getLength();
    }
}
