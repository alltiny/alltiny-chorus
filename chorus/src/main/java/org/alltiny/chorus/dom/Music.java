package org.alltiny.chorus.dom;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008 19:19:03
 */
public class Music {

    private final List<Voice> voices = new ArrayList<Voice>();

    public void addVoice(Voice voice) {
        voices.add(voice);
    }

    public List<Voice> getVoices() {
        return voices;
    }
}
