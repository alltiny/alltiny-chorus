package org.alltiny.chorus.dom;

/**
 * This is a super class of all elements with a duration.
 * This class define the duration as a ratio of a denominator
 * and a divisior, herein called duration and division. This
 * class allows to define all durations from 1/32 up to 3/2;
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 17.12.2007 18:44:05
 */
public class DurationElement<Self extends DurationElement<Self>> extends Element<Self> {

    public enum Property {
        DURATION,
        DIVISION
    }

    public DurationElement() {
        this(0, 0);
    }

    public DurationElement(int duration, int division) {
        setDuration(duration);
        setDivision(division);
    }

    public int getDuration() {
        return (int)get(Property.DURATION.name());
    }

    public Self setDuration(int duration) {
        put(Property.DURATION.name(), duration);
        checkForDividability();
        return (Self)this;
    }

    public int getDivision() {
        return (int)get(Property.DIVISION.name());
    }

    public Self setDivision(int division) {
        put(Property.DIVISION.name(), division);
        checkForDividability();
        return (Self)this;
    }

    /**
     * This method checks that the given duration and division cannot
     * be divided any further.
     * The length of the element is given as ratio of a duration and
     * a division. A full note is 1/1; a half note is 1/2; and so on.
     */
    protected void checkForDividability() {
        if (get(Property.DURATION.name()) == null || get(Property.DIVISION.name()) == null) {
            return;
        }
        // check for dividable values.
        while (getDuration() > 0 && getDivision() > 0 && (getDuration() % 2) == 0 && (getDivision() % 2) == 0) {
            setDuration(getDuration() / 2);
            setDivision(getDivision() / 2);
        }
    }

    public double getLength() {
        return ((double)getDuration()) / getDivision();
    }
}
