package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Container element for all the voices.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLMusicV1 {

    @XmlElement(name = "voice")
    private final List<XMLVoiceV1> voices = new ArrayList<XMLVoiceV1>();

    public void addVoice(XMLVoiceV1 voice) {
        voices.add(voice);
    }

    public List<XMLVoiceV1> getVoices() {
        return voices;
    }
}
