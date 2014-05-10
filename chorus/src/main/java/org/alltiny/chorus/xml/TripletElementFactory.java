package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.decoration.Triplet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008 20:51:52
 */
public class TripletElementFactory implements XMLFactory<Triplet> {

    private final Attributes attributes;

    public TripletElementFactory(final Attributes attributes) {
        this.attributes = attributes;
    }

    public Triplet createInstance() throws SAXException {
        int ref;
        try {
            ref = Integer.parseInt(attributes.getValue("ref"));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'ref\' is undefined or wrong. only numbers are allowed.", e);
        }

        return new Triplet(ref);
    }
}