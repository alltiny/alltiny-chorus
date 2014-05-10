package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.DurationElement;
import org.alltiny.xml.handler.AssignHandler;
import org.alltiny.xml.handler.XMLHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 * @param <Type> real duration type this handler should work for.
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 13:49:35
 */
public class DurationElementXMLHandler<Type extends DurationElement> extends XMLHandler<Type> {

    private final Type element;

    public DurationElementXMLHandler(final Type element, AssignHandler<Type> assignHandler, Attributes attributes) throws SAXException {
        super(assignHandler);
        this.element = element;

        try {
            element.setDuration(Integer.parseInt(attributes.getValue("duration")));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'duration\' is undefined or wrong. only numbers are allowed.", e);
        }
        try {
            element.setDivision(Integer.parseInt(attributes.getValue("division")));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'division\' is undefined or wrong. only numbers are allowed.", e);
        }
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new SAXException("No child elements supposed.");
    }

    public Type getObject() {
        return element;
    }
}