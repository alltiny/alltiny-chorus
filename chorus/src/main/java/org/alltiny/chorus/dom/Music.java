package org.alltiny.chorus.dom;

import org.alltiny.chorus.model.generic.DOMList;
import org.alltiny.chorus.model.generic.DOMMap;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * The music data of a {@link Song}.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008
 */
public class Music extends DOMMap<Music,Object> {

    public enum Property {
        VOICES
    }

    public Music() {
        put(Property.VOICES.name(), new DOMList<DOMList<?,Voice>,Voice>());
    }

    public DOMList<DOMList<?,Voice>,Voice> getVoices() {
        return (DOMList<DOMList<?,Voice>,Voice>)get(Property.VOICES.name());
    }

    public Music setVoices(Collection<Voice> voices) {
        getVoices().clear();
        getVoices().addAll(voices);
        return this;
    }

    public void addVoice(Voice voice) {
        getVoices().add(voice);
    }
}
