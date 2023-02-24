package org.alltiny.chorus.dom;

import org.alltiny.chorus.base.type.AccidentalSign;
import org.alltiny.chorus.base.type.BaseNote;

import java.text.ParseException;

/**
 * This class parses a string to a note.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 12:01:36
 */
public class NoteMidiValue {

    /**
     * This method parses the given value, assuming as international notation.
     * The international notation of a note follows following scheme:
     *
     *     {A,B,C,D,E,F,G} [{#,b}] <int>
     *
     * For example: A4 (440Hz) is C#4
     */
    public static Note createNote(String value, int duration, int division) throws ParseException {
        BaseNote note = null;
        // determine the base-value of this note.
        if (value.indexOf('C') > -1) {
            note = BaseNote.C;
        }
        if (value.indexOf('D') > -1) {
            note = BaseNote.D;
        }
        if (value.indexOf('E') > -1) {
            note = BaseNote.E;
        }
        if (value.indexOf('F') > -1) {
            note = BaseNote.F;
        }
        if (value.indexOf('G') > -1) {
            note = BaseNote.G;
        }
        if (value.indexOf('A') > -1) {
            note = BaseNote.A;
        }
        if (value.indexOf('B') > -1) {
            note = BaseNote.B;
        }

        // break if the base value of this note could be determined.
        if (note == null) {
            throw new ParseException("Base Value {A,B,C,D,E,F,G} not defined in \'" + value + "\'", 0);
        }

        AccidentalSign sign = AccidentalSign.NONE;
        // check for modification.
        if (value.indexOf('#') > -1) {
            sign = AccidentalSign.SHARP;
        }
        if (value.indexOf('b') > -1) {
            sign = AccidentalSign.FLAT;
        }
        if (value.indexOf("##") > -1) {
            sign = AccidentalSign.DSHARP;
        }
        if (value.indexOf("x") > -1) {
            sign = AccidentalSign.DSHARP;
        }
        if (value.indexOf("bb") > -1) {
            sign = AccidentalSign.DFLAT;
        }
        /*if (value.indexOf("n") > -1) {
            sign = AccidentalSign.NATURAL;
        }*/

        // determine the octave of the note.
        Integer octave = null;
        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(i);
            if (Character.isDigit(digit)) {
                octave = Character.digit(digit, 10);
            }
        }

        // break if the base value of this note could be determined.
        if (octave == null) {
            throw new ParseException("Octave not defined in \'" + value + "\'", 0);
        }

        return new Note(note, sign, octave, duration, division);
    }
}
