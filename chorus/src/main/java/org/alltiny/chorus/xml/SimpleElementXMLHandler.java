package org.alltiny.chorus.xml;

import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 * @param <Type> type this handler should work for.
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 13:49:35
 */
public class SimpleElementXMLHandler<Type> extends XMLHandler<Type> {

    private final Type element;

    public SimpleElementXMLHandler(final Type element, AssignHandler<Type> assignHandler) {
        super(assignHandler);
        this.element = element;
    }

    public SimpleElementXMLHandler(final XMLFactory<? extends Type> factory, AssignHandler<Type> assignHandler) throws SAXException {
        super(assignHandler);
        this.element = factory.createInstance();
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new SAXException("No child elements supposed.");
    }

    public Type getObject() {
        return element;
    }
}
