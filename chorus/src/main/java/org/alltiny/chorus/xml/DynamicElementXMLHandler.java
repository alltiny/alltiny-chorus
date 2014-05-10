package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.DynamicElement;
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
public class DynamicElementXMLHandler extends XMLHandler<DynamicElement> {

    private final DynamicElement element;

    public DynamicElementXMLHandler(final Attributes attributes, AssignHandler<DynamicElement> assignHandler) throws SAXException {
        super(assignHandler);

        String key = attributes.getValue("value");
        if (key == null) {
            throw new SAXException("Attribute 'value' is missing in element 'dynamic'.");
        }

        element = new DynamicElement(key);
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new SAXException("No child element allowed in 'dynamic'.");
    }

    public DynamicElement getObject() {
        return element;
    }
}
