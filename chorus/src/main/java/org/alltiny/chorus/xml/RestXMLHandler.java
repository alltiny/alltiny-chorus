package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Rest;
import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 11:58:22
 */
public class RestXMLHandler extends XMLHandler<Rest> {

    private final Rest rest;

    public RestXMLHandler(final Attributes attributes, AssignHandler<Rest> assignHandler) throws SAXException {
        super(assignHandler);
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

        rest = new Rest(duration, division);
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new SAXException("No child element allowed in \'rest\'.");
    }

    public Rest getObject() {
        return rest;
    }
}
