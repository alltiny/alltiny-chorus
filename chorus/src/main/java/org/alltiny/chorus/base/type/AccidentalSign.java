package org.alltiny.chorus.base.type;

/**
 * This enum defines the different options for accidental signs.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 */
public enum AccidentalSign {
    NONE(0),   // none accidental sign
    SHARP(1),  // #
    FLAT(-1),  // b
    NATURAL(0),// release
    DSHARP(2), // x (##)
    DFLAT(-2); // bb

    private final int value;

    private AccidentalSign(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AccidentalSign parse(String value) {
        switch (value) {
            case "b": return FLAT;
            case "bb": return DFLAT;
            case "#": return SHARP;
            case "x": // fall through to "##"
            case "##": return DSHARP;
            default: return NONE;
        }
    }
}
