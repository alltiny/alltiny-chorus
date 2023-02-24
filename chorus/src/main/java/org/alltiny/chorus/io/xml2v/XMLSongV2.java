package org.alltiny.chorus.io.xml2v;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import org.alltiny.chorus.io.xmlv1.XMLMusicV1;

import java.util.Properties;

/**
 * An XMl representation of a song-element.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 21.02.2023
 */
@XmlRootElement(name = "song")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLSongV2 {

    public static final String NAMESPACE = "http://chorus.alltiny.org/chorus-music-v2";

    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "title")
    private String author;
    @XmlElement(name = "meta")
    private Properties meta = new Properties();
    @XmlElement(name = "tempo")
    private int tempo;
    @XmlElement(name = "music-data")
    private XMLMusicV1 music;

    public String getTitle() {
        return title;
    }

    public XMLSongV2 setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public XMLSongV2 setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Properties getMeta() {
        return meta;
    }

    public XMLSongV2 setMeta(Properties meta) {
        this.meta = meta;
        return this;
    }

    public XMLMusicV1 getMusic() {
        return music;
    }

    public XMLSongV2 setMusic(XMLMusicV1 music) {
        this.music = music;
        return this;
    }

    public int getTempo() {
        return tempo;
    }

    public XMLSongV2 setTempo(int tempo) {
        this.tempo = tempo;
        return this;
    }
}
