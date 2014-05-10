package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.dom.Music;
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
public class SongXMLHandler extends XMLHandler<Song> {

    private final Song song = new Song();

    public SongXMLHandler(AssignHandler<Song> assignHandler) {
        super(assignHandler);
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("title".equals(qName)) {
            return new StringXMLHandler(new AssignHandler<String>() {
                public void assignNode(String node) {
                    song.setTitle(node);
                }
            });
        }
        if ("author".equals(qName)) {
            return new StringXMLHandler(new AssignHandler<String>() {
                public void assignNode(String node) {
                    song.setAuthor(node);
                }
            });
        }
        if ("meta".equals(qName)) {
            return new MetaXMLHandler(new AssignHandler<Properties>() {
                public void assignNode(Properties node) {
                    song.setMeta(node);
                }
            });
        }
        if ("music-data".equals(qName)) {
            return new MusicXMLHandler(new AssignHandler<Music>() {
                public void assignNode(Music node) {
                    song.setMusic(node);
                }
            });
        }
        if ("tempo".equals(qName)) {
            return new StringXMLHandler(new AssignHandler<String>() {
                public void assignNode(String node) {
                    song.setTempo(Integer.parseInt(node));
                }
            });
        }

        // throw an exception if this point is reached.
        throw new SAXException("Element \'" + qName + "\' could not be resolved as child in \'song\'");
    }

    public Song getObject() {
        return song;
    }
}
