package org.alltiny.chorus.dom.decoration;

import org.alltiny.chorus.base.type.AccidentalSign;

/**
 * Defines which accidental sign should be rendered.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008
 */
public class Accidental extends Decoration<Accidental> {

    public enum Property {
        SIGN
    }
    private AccidentalSign sign = AccidentalSign.NONE;

    public Accidental() {
        setSign(AccidentalSign.NONE);
    }

    public Accidental(AccidentalSign sign) {
        setSign(sign);
    }

    public AccidentalSign getSign() {
        return (AccidentalSign)get(Property.SIGN.name());
    }

    public Accidental setSign(AccidentalSign sign) {
        put(Property.SIGN.name(), sign);
        return this;
    }
}
