package org.alltiny.chorus.model;

import org.alltiny.chorus.dom.Song;

import java.beans.PropertyChangeListener;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 04.02.2009 21:32:37
 */
public interface SongModel {

    public static final String CURRENT_SONG = "currentSong";

    public void setSong(Song song);
    public Song getSong();

    public void addPropertyChangeListener(String propName, PropertyChangeListener listener);
}
