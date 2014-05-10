package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Music;
import org.alltiny.chorus.dom.Voice;
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
public class MusicXMLHandler extends XMLHandler<Music> {

    private final Music music = new Music();

    public MusicXMLHandler(AssignHandler<Music> assignHandler) {
        super(assignHandler);
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("voice".equals(qName)) {
            return new VoiceXMLHandler(attributes, new AssignHandler<Voice>() {
                public void assignNode(Voice node) {
                    music.addVoice(node);
                }
            });
        }

        // throw an exceptio if this point is reached.
        throw new SAXException("Element \'" + qName + "\' could not be resolved as child in \'song\'");
    }

    public Music getObject() {
        return music;
    }
}
