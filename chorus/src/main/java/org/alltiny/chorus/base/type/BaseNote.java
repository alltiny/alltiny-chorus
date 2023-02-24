package org.alltiny.chorus.base.type;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 11.02.2009 20:48:36
 */
public enum BaseNote {
    C(12),D(14),E(16),F(17),G(19),A(21),B(23);

    private final int value;

    private BaseNote(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
