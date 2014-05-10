package org.alltiny.xml.handler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.InputStream;

/**
 * This class is the entry point into xml parsing with this module.
 */
public class XMLParser {

    private XMLParser() {} // no need to instantiate it.

    /**
     * This method creates a {@link SAXParser} instance and starts parsing with
     * the given {@param rootHandler}.
     *
     * @param xmlStream is supposed to be a stream of xml.
     * @param rootHandler is the xml handler of the root element.
     */
    public static <Type> Type parseXML(InputStream xmlStream, XMLHandler<Type> rootHandler) throws Exception {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

        rootHandler.setReader(parser.getXMLReader());
        parser.parse(xmlStream, rootHandler);

        return rootHandler.getObject();
    }
}
