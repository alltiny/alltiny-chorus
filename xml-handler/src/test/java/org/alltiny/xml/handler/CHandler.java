package org.alltiny.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handler which should parse objects of {@link C}.
 */
public class CHandler extends XMLHandler<C> {

    private final C element;

    public CHandler(AssignHandler<C> assignHandler) {
        super(assignHandler);
        element = new C();
    }

    public C getObject() {
        return element;
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new SAXException("This node does not allow any children. Found: \'" + qName + "\'");
    }
}
