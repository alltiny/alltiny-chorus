package org.alltiny.chorus.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 13:49:35
 */
public class StringXMLHandler extends XMLHandler<String> {

    private String text = "";

    public StringXMLHandler(AssignHandler<String> assignHandler) {
        super(assignHandler);
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        text = new String(ch, start, length);
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new SAXException("No child elements supposed.");
    }

    public String getObject() {
        return text;
    }
}
