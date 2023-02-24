package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.ValidationException;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.alltiny.chorus.base.type.AccidentalSign;
import org.alltiny.chorus.base.type.BaseNote;
import org.alltiny.chorus.base.type.NoteValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mapper for {@link NoteValue}
 *
 * @since 4.0
 */
public class NoteValueAdapter extends XmlAdapter<String,NoteValue> {

    private static final Pattern pattern = Pattern.compile("(?<base>[ABCDEFG])(?<sign>((b)|(bb)|(x)|(#)|(##))?)(?<octave>\\d)");
    /**
     * This method parses the given value, assuming as international notation.
     * The international notation of a note follows ths scheme:
     *
     *     {A,B,C,D,E,F,G} [{#,b}] <int>
     *
     * For example: A4 (440Hz) is C#4
     */
    @Override
    public NoteValue unmarshal(String value) throws ValidationException {
        final Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            return new NoteValue(
                BaseNote.valueOf(matcher.group("base")),
                AccidentalSign.parse(matcher.group("sign")),
                Integer.parseInt(matcher.group("octave"))
            );
        } else {
            throw new ValidationException("value '" + value + "' could not be parsed as a note-value. Expected pattern is (A|B|C|D|E|F|G)((b)|(bb)|(x)|(#)|(##))?/d");
        }
    }

    @Override
    public String marshal(NoteValue value) {
        final String accidentalSign;
        switch (value.getSign().getValue()) {
            case -2: accidentalSign = "bb"; break;
            case -1: accidentalSign = "b"; break;
            case  1: accidentalSign = "#"; break;
            case  2: accidentalSign = "##"; break;
            default: accidentalSign = ""; break; // covers also the 0-case
        }
        return value.getBaseNote().name() + accidentalSign + value.getOctave();
    }
}
