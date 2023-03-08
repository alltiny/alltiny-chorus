package org.alltiny.chorus.dom;

import org.alltiny.chorus.model.generic.DOMMap;

/**
 * Carries the properties of a song.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008
 */
public class Song extends DOMMap<Song,Object> {

    public enum Property {
        TITLE,
        AUTHOR,
        META,
        MUSIC,
        TEMPO,
        DYNAMIC,
        FILENAME
    }

    public Song() {
        Dynamic dynamic = new Dynamic();
        dynamic.addValue("fff", 92);
        dynamic.addValue("ff",  84);
        dynamic.addValue("f",   76);
        dynamic.addValue("mf",  68);
        dynamic.addValue("mp",  60);
        dynamic.addValue("p",   52);
        dynamic.addValue("pp",  44);
        dynamic.addValue("ppp", 36);
        put(Property.DYNAMIC.name(), dynamic);
    }

    public String getTitle() {
        return (String)get(Property.TITLE.name());
    }

    public Song setTitle(String title) {
        put(Property.TITLE.name(), title);
        return this;
    }

    public String getAuthor() {
        return (String)get(Property.AUTHOR.name());
    }

    public Song setAuthor(String author) {
        put(Property.AUTHOR.name(), author);
        return this;
    }

    public DOMMap<?,String> getMeta() {
        return (DOMMap<?, String>)get(Property.META.name());
    }

    public Song setMeta(DOMMap<?,String> meta) {
        put(Property.META.name(), meta);
        return this;
    }

    public Music getMusic() {
        return (Music)get(Property.MUSIC.name());
    }

    public Song setMusic(Music music) {
        put(Property.MUSIC.name(), music);
        return this;
    }

    public int getTempo() {
        return (int)get(Property.TEMPO.name());
    }

    public Song setTempo(int tempo) {
        put(Property.TEMPO.name(), tempo);
        return this;
    }

    public String getFilename() {
        return (String)get(Property.FILENAME.name());
    }

    public Song setFilename(String name) {
        put(Property.FILENAME.name(), name);
        return this;
    }

    public Dynamic getDynamic() {
        return (Dynamic)get(Property.DYNAMIC.name());
    }
}
