package org.alltiny.xml.handler;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

/**
 * Handler which should parse objects of {@link A}.
 */
public class AHandler extends XMLHandler<A> implements AssignHandler<B> {

    private final A element;

    public AHandler(AssignHandler<A> assignHandler) {
        super(assignHandler);
        element = new A();
    }

    public A getObject() {
        return element;
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("b".equals(qName)) {
            return new BHandler(this);
        }

        throw new SAXException("Node \'" + qName + "\' could not be resolved.");
    }

    public void assignNode(B node) throws AssignException {
        element.setChild(node);
    }
}
