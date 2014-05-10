package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.dom.Sequence;
import org.alltiny.chorus.dom.InlineSequence;
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
public class VoiceXMLHandler extends XMLHandler<Voice> {

    private final Voice voice = new Voice();

    public VoiceXMLHandler(final Attributes attributes, AssignHandler<Voice> assignHandler) {
        super(assignHandler);
        voice.setName(attributes.getValue("name"));
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("head".equals(qName)) {
            return new StringXMLHandler(new AssignHandler<String>() {
                public void assignNode(String node) {
                    voice.setHead(node);
                }
            });
        }
        if ("sequence".equals(qName)) {
            return new SequenceXMLHandler(attributes, new AssignHandler<Sequence>() {
                public void assignNode(Sequence node) {
                    voice.setSequence(node);
                }
            });
        }
        if ("inlinesequence".equals(qName)) {
            return new InlineSequenceXMLHandler(attributes, new AssignHandler<InlineSequence>() {
                public void assignNode(InlineSequence node) {
                    voice.addInlineSequence(node);
                }
            });
        }

        // throw an exception if this point is reached.
        throw new SAXException("Element \'" + qName + "\' could not be resolved as child in \'song\'");
    }

    public Voice getObject() {
        return voice;
    }
}
