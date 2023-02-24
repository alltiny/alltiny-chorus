package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * This is a super class of all elements with a duration.
 * This class define the duration as a ratio of a denominator
 * and a divisor, herein called duration and division. This
 * class allows to define all durations from 1/32 up to 3/2;
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLDurationElementV1 extends XMLElementV1 {

    @XmlAttribute
    private int duration; // duration of this element.
    @XmlAttribute
    private int division;

    public int getDuration() {
        return duration;
    }

    public XMLDurationElementV1 setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getDivision() {
        return division;
    }

    public XMLDurationElementV1 setDivision(int division) {
        this.division = division;
        return this;
    }
}
