package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Bar;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008 20:51:52
 */
public class BarElementFactory implements XMLFactory<Bar> {

    private final Attributes attributes;

    public BarElementFactory(final Attributes attributes) {
        this.attributes = attributes;
    }

    public Bar createInstance() throws SAXException {
        int duration, division;
        try {
            duration = Integer.parseInt(attributes.getValue("duration"));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'duration\' is undefined or wrong. only numbers are allowed.", e);
        }
        try {
            division = Integer.parseInt(attributes.getValue("division"));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'division\' is undefined or wrong. only numbers are allowed.", e);
        }

        Bar bar = new Bar(duration, division);

        if (attributes.getValue("keepBeatDuration") != null) {
            bar.setKeepBeatDuration(Boolean.parseBoolean(attributes.getValue("keepBeatDuration")));
        }

        return bar;
    }
}
