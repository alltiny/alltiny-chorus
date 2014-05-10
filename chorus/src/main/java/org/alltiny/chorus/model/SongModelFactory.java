package org.alltiny.chorus.model;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 04.02.2009 21:34:31
 */
public class SongModelFactory {

    public static SongModel createInstance() {
        return new SongBean();
    }
}
