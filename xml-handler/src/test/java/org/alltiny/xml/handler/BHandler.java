package org.alltiny.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handler which should parse objects of {@link B}.
 */
public class BHandler extends XMLHandler<B> implements AssignHandler<C> {

    private final B element;

    public BHandler(AssignHandler<B> assignHandler) {
        super(assignHandler);
        element = new B();
    }

    public B getObject() {
        return element;
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("c".equals(qName)) {
            return new CHandler(this);
        }

        throw new SAXException("Node \'" + qName + "\' could not be resolved.");
    }

    public void assignNode(C node) throws AssignException {
        element.addChild(node);
    }
}
