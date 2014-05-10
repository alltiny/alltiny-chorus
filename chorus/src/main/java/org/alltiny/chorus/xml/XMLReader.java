package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Song;
import org.alltiny.xml.handler.XMLParser;
import org.alltiny.xml.handler.XMLDocument;

import java.io.InputStream;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 01.12.2008 19:28:36
 */
public class XMLReader {

    public static Song readSongFromXML(InputStream xmlStream) throws Exception {
        return XMLParser.parseXML(xmlStream, new XMLDocument<Song>(SongXMLHandler.class));
    }
}
