package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Bar;
import org.alltiny.xml.handler.AssignHandler;
import org.alltiny.xml.handler.XMLHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 11:58:22
 */
public class BarXMLHandler extends XMLHandler<Bar> {

    private final Bar bar;

    public BarXMLHandler(final Attributes attributes, AssignHandler<Bar> assignHandler) throws SAXException {
        super(assignHandler);
        bar = new BarElementFactory(attributes).createInstance();
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return null;
    }

    public Bar getObject() {
        return bar;
    }
}