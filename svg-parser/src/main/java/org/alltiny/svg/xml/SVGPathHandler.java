package org.alltiny.svg.xml;

import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.alltiny.svg.dom.SVGPath;
import org.alltiny.svg.parser.SVGPathParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.04.2009
 */
public class SVGPathHandler extends XMLHandler {

    private SVGPath path;

    public SVGPathHandler(final Attributes attributes, AssignHandler assignHandler) throws SAXException {
        super(assignHandler);

        // check the path definition (mandatory)
        String d = attributes.getValue("d");
        if (d == null) {
            throw new SAXException("Attribute \'d\' was defined, but is required.");
        }

        try {
            path = new SVGPath(SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream(d.getBytes()))));
        } catch (Exception ex) {
            throw new SAXException("Attribute \'d\' was not proper defined.", ex);
        }
    }

    public Object getObject() {
        return path;
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new SAXException("Element \'path\' does not allow any children.");
    }
}
