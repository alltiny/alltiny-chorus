package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An XMl representation of a song-element.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlRootElement(name = "song")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLSongV1 {

    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "author")
    private String author;
    @XmlElement(name = "meta")
    @XmlJavaTypeAdapter(MapAdapter.class)
    private Map<String,String> meta = new LinkedHashMap<>();
    @XmlElement(name = "tempo")
    private int tempo;
    @XmlElement(name = "music-data")
    private XMLMusicV1 music;

    public String getTitle() {
        return title;
    }

    public XMLSongV1 setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public XMLSongV1 setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Map<String,String> getMeta() {
        return meta;
    }

    public XMLSongV1 setMeta(Map<String,String> meta) {
        this.meta.clear();
        this.meta.putAll(meta);
        return this;
    }

    public XMLSongV1 setMeta(String name, String property) {
        this.meta.put(name, property);
        return this;
    }

    public XMLMusicV1 getMusic() {
        return music;
    }

    public XMLSongV1 setMusic(XMLMusicV1 music) {
        this.music = music;
        return this;
    }

    public int getTempo() {
        return tempo;
    }

    public XMLSongV1 setTempo(int tempo) {
        this.tempo = tempo;
        return this;
    }
}
