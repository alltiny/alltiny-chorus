package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.ValidationException;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.alltiny.chorus.base.type.AccidentalSign;

/**
 * Mapper for {@link AccidentalSign}
 *
 * @since 4.0
 */
public class AccidentalSignAdapter extends XmlAdapter<String,AccidentalSign> {

    /**
     * This method parses the given value, assuming as international notation.
     * The international notation of a note follows ths scheme:
     *
     *     {A,B,C,D,E,F,G} [{#,b}] <int>
     *
     * For example: A4 (440Hz) is C#4
     */
    @Override
    public AccidentalSign unmarshal(String value) throws ValidationException {
        switch (value) {
            case "none": return AccidentalSign.NONE;
            case "natural": return AccidentalSign.NATURAL;
            case "sharp": return AccidentalSign.SHARP;
            case "flat": return AccidentalSign.FLAT;
            case "dsharp": return AccidentalSign.DSHARP;
            case "dflat": return AccidentalSign.DFLAT;
            default: throw new ValidationException("value '" + value + "' could not be mapped to an accidental sign. " +
                "Use one of 'none', 'natural', 'sharp', 'flat', 'dsharp' or 'dflat'");
        }
    }

    @Override
    public String marshal(AccidentalSign value) {
        switch (value) {
            case NATURAL: return "natural";
            case SHARP: return "sharp";
            case FLAT: return "flat";
            case DSHARP: return "dsharp";
            case DFLAT: return "dflat";
            default: return "none";
        }
    }
}
