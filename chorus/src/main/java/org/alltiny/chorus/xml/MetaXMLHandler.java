package org.alltiny.chorus.xml;

import org.alltiny.chorus.model.generic.DOMMap;
import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 13:42:57
 */
public class MetaXMLHandler extends XMLHandler<DOMMap<?,String>> {

    private final DOMMap<?,String> meta = new DOMMap<>();

    public MetaXMLHandler(AssignHandler<DOMMap<?,String>> assignHandler) {
        super(assignHandler);
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return new StringXMLHandler(new MetaAssignHandler(meta, qName));
    }

    public DOMMap<?,String> getObject() {
        return meta;
    }

    private static class MetaAssignHandler implements AssignHandler<String> {

        private final DOMMap<?,String> properties;
        private final String name;

        public MetaAssignHandler(final DOMMap<?,String> properties, final String name) {
            this.properties = properties;
            this.name = name;
        }

        public void assignNode(String node) {
            properties.put(name, node);
        }
    }
}
