package org.alltiny.chorus.model;

import org.alltiny.chorus.dom.Song;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 04.02.2009 21:34:42
 */
/*package-local*/class SongBean implements SongModel {

    private final PropertyChangeSupport pcs;

    private Song song;

    public SongBean() {
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propName, listener);
    }

    public void setSong(Song newSong) {
        Song old = song;
        song = newSong;
        pcs.firePropertyChange(SongModel.CURRENT_SONG, old, song);
    }

    public Song getSong() {
        return song;
    }
}
