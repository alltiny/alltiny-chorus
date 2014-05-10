package org.alltiny.chorus.xml;

import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Properties;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 13:42:57
 */
public class MetaXMLHandler extends XMLHandler<Properties> {

    private final Properties meta = new Properties();

    public MetaXMLHandler(AssignHandler<Properties> assignHandler) {
        super(assignHandler);
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return new StringXMLHandler(new MetaAssignHandler(meta, qName));
    }

    public Properties getObject() {
        return meta;
    }

    private static class MetaAssignHandler implements AssignHandler<String> {

        private final Properties properties;
        private final String name;

        public MetaAssignHandler(final Properties properties, final String name) {
            this.properties = properties;
            this.name = name;
        }

        public void assignNode(String node) {
            properties.put(name, node);
        }
    }
}
