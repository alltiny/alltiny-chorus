package org.alltiny.chorus.dom.decoration;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 22:59:18
 */
public class Accidental extends Decoration {

    private AccidentalSign sign = AccidentalSign.NONE;

    public Accidental() {}

    public Accidental(AccidentalSign sign) {
        this.sign = sign;
    }

    public AccidentalSign getSign() {
        return sign;
    }

    public void setSign(AccidentalSign sign) {
        this.sign = sign;
    }
}
