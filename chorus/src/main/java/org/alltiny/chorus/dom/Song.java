package org.alltiny.chorus.dom;

import java.util.Properties;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 13:42:36
 */
public class Song {

    private String title;
    private String author;
    private Properties meta = new Properties();
    private Music music;
    private int tempo;
    private Dynamic dynamic;

    public Song() {
        dynamic = new Dynamic();
        dynamic.addValue("fff", 92);
        dynamic.addValue("ff",  84);
        dynamic.addValue("f",   76);
        dynamic.addValue("mf",  68);
        dynamic.addValue("mp",  60);
        dynamic.addValue("p",   52);
        dynamic.addValue("pp",  44);
        dynamic.addValue("ppp", 36);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Properties getMeta() {
        return meta;
    }

    public void setMeta(Properties meta) {
        this.meta = meta;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }
}
