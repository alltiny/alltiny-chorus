package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.AbsoluteTempoChange;
import org.alltiny.chorus.dom.DurationElement;
import org.alltiny.xml.handler.AssignException;
import org.alltiny.xml.handler.AssignHandler;
import org.alltiny.xml.handler.XMLHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 25.01.2011 17:00
 */
public class AbsoluteTempoChangeXMLHandler extends XMLHandler<AbsoluteTempoChange> {

    private final AbsoluteTempoChange tempoChange = new AbsoluteTempoChange();

    public AbsoluteTempoChangeXMLHandler(AssignHandler<AbsoluteTempoChange> assignHandler, Attributes attributes) throws SAXException {
        super(assignHandler);
        try {
            tempoChange.setNumberPerMinute(Integer.parseInt(attributes.getValue("numberPerMinute")));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'numberPerMinute\' is undefined or wrong. only numbers are allowed.", e);
        }
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("note".equals(qName)) {
            return new DurationElementXMLHandler<DurationElement>(new DurationElement(), new AssignHandler<DurationElement>() {
                public void assignNode(DurationElement node) throws AssignException {
                    tempoChange.setNote(node);
                }
            }, attributes);
        }
        return null;
    }

    public AbsoluteTempoChange getObject() {
        return tempoChange;
    }
}