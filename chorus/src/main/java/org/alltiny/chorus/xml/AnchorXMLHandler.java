package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Anchor;
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
public class AnchorXMLHandler extends XMLHandler<Anchor> {

    private final Anchor anchor;

    public AnchorXMLHandler(final Attributes attributes, AssignHandler<Anchor> assignHandler) throws SAXException {
        super(assignHandler);
        try {
            anchor = new Anchor().setRef(Integer.parseInt(attributes.getValue("ref")));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'ref\' is undefined or wrong. only numbers are allowed.", e);
        }
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return null;
    }

    public Anchor getObject() {
        return anchor;
    }
}
