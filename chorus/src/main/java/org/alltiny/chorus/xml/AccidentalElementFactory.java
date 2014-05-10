package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.decoration.Accidental;
import org.alltiny.chorus.dom.decoration.AccidentalSign;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008 20:51:52
 */
public class AccidentalElementFactory implements XMLFactory<Accidental> {

    private final Attributes attributes;

    public AccidentalElementFactory(final Attributes attributes) {
        this.attributes = attributes;
    }

    public Accidental createInstance() throws SAXException {
        String type = attributes.getValue("type");

        if (type == null) {
            throw new SAXException("Attribute \'type\' is undefined or wrong. only numbers are allowed.");
        }

        if ("none".equals(type)) {
            return new Accidental(AccidentalSign.NONE);
        }
        if ("natural".equals(type)) {
            return new Accidental(AccidentalSign.NATURAL);
        }
        if ("sharp".equals(type)) {
            return new Accidental(AccidentalSign.SHARP);
        }
        if ("flat".equals(type)) {
            return new Accidental(AccidentalSign.FLAT);
        }
        if ("dsharp".equals(type)) {
            return new Accidental(AccidentalSign.DSHARP);
        }
        if ("dflat".equals(type)) {
            return new Accidental(AccidentalSign.DFLAT);
        }

        throw new SAXException("Attribute \'type\' is undefined or wrong. only numbers are allowed.");
    }
}
