package org.alltiny.svg.xml;

import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.04.2009
 */
public class SVGElementHandler extends XMLHandler {

    public SVGElementHandler(AssignHandler assignHandler) {
        super(assignHandler);
    }

    public Object getObject() {
        return null;
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return null;
    }
}
