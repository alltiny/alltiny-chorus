package org.alltiny.chorus.dom.decoration;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 19:29:28
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
}
