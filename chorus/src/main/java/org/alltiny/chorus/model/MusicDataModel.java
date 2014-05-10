package org.alltiny.chorus.model;

import java.util.List;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.02.2009 18:34:21
 */
public interface MusicDataModel {

    public int getNumberOfFrames();
    public int getNumberOfVoices();
    public List<Frame> getFrames();
}
