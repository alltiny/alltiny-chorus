package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.DurationElement;
import org.alltiny.chorus.dom.RelativeTempoChange;
import org.alltiny.xml.handler.AssignException;
import org.alltiny.xml.handler.AssignHandler;
import org.alltiny.xml.handler.XMLHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 11:58:22
 */
public class RelativeTempoChangeXMLHandler extends XMLHandler<RelativeTempoChange> {

    private final RelativeTempoChange tempoChange = new RelativeTempoChange();

    public RelativeTempoChangeXMLHandler(AssignHandler<RelativeTempoChange> assignHandler) throws SAXException {
        super(assignHandler);
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("left".equals(qName)) {
            return new DurationElementXMLHandler<DurationElement>(new DurationElement(), new AssignHandler<DurationElement>() {
                public void assignNode(DurationElement node) throws AssignException {
                    tempoChange.setLeft(node);
                }
            }, attributes);
        } else if ("right".equals(qName)) {
            return new DurationElementXMLHandler<DurationElement>(new DurationElement(), new AssignHandler<DurationElement>() {
                public void assignNode(DurationElement node) throws AssignException {
                    tempoChange.setRight(node);
                }
            }, attributes);
        }
        return null;
    }

    public RelativeTempoChange getObject() {
        return tempoChange;
    }
}