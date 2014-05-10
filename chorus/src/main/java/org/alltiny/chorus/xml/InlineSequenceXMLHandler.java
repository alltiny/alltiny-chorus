package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.*;
import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.alltiny.xml.handler.AssignException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 13:42:57
 */
public class InlineSequenceXMLHandler extends XMLHandler<InlineSequence> {

    private final InlineSequence sequence;

    public InlineSequenceXMLHandler(Attributes attributes, AssignHandler<InlineSequence> assignHandler) throws SAXException {
        super(assignHandler);
        sequence = new InlineSequence(new Anchor(Integer.parseInt(attributes.getValue("anchorref"))));
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("note".equals(qName)) {
            return new NoteXMLHandler(attributes, new SequenceAssignHandler<Note>(sequence));
        }
        if ("rest".equals(qName)) {
            return new RestXMLHandler(attributes, new SequenceAssignHandler<Rest>(sequence));
        }
        if ("dynamic".equals(qName)) {
            return new DynamicElementXMLHandler(attributes, new SequenceAssignHandler<DynamicElement>(sequence));
        }

        // throw an exception if this point is reached.
        throw new SAXException("Element \'" + qName + "\' is not allowed in InlineSequence.");
    }

    public InlineSequence getObject() {
        return sequence;
    }

    private static class SequenceAssignHandler<Type extends Element> implements AssignHandler<Type> {

        private final InlineSequence sequence;

        public SequenceAssignHandler(InlineSequence sequence) {
            this.sequence = sequence;
        }

        public void assignNode(Type node) throws AssignException {
            sequence.addElement(node);
        }
    }
}
