package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * This class represents an absolute tempo definition like
 * "66 quarter note per minute".
 * A tempo factor is used to define temporal changes on a bar line.
 * A tempo factor can be defined like: "3/4=1/2" which means
 * "a dotted half note has the same duration as a half note".
 *
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLAbsoluteTempoChangeV1 extends XMLElementV1 {

    @XmlElement
    private XMLDurationElementV1 note;

    @XmlAttribute
    private int numberPerMinute;

    public XMLDurationElementV1 getNote() {
        return note;
    }

    /**
     * Sets the note type.
     */
    public XMLAbsoluteTempoChangeV1 setNote(XMLDurationElementV1 note) {
        this.note = note;
        return this;
    }

    public int getNumberPerMinute() {
        return numberPerMinute;
    }

    /**
     * Sets how many {@link #note}s shall be played in a minute.
     */
    public XMLAbsoluteTempoChangeV1 setNumberPerMinute(int numberPerMinute) {
        this.numberPerMinute = numberPerMinute;
        return this;
    }
}
