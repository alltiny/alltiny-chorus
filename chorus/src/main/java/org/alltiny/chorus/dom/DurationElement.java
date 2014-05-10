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
public class DurationElement extends Element {

    private int duration; // duration of this element.
    private int division;

    public DurationElement() {}

    public DurationElement(int duration, int division) {
        setDuration(duration);
        setDivision(division);
    }

    /** Copy constructor. */
    public DurationElement(DurationElement element) {
        setDuration(element.getDuration());
        setDivision(element.getDivision());
    }

    public int getDuration() {
        return duration;
    }

    public DurationElement setDuration(int duration) {
        this.duration = duration;
        checkForDividability();
        return this;
    }

    public int getDivision() {
        return division;
    }

    public DurationElement setDivision(int division) {
        this.division = division;
        checkForDividability();
        return this;
    }

    /**
     * This method checks that the given duration and division are not
     * dividable any further more.
     * The length of the element is given as ratio of a duration and
     * a division. A full note is 1/1; a half note is 1/2; and so on.
     */
    private void checkForDividability() {
        // avoid dividable values.
        while (duration > 0 && division > 0 && (duration % 2) == 0 && (division % 2) == 0) {
            duration /= 2;
            division /= 2;
        }
    }

    public double getLength() {
        return ((double)duration) / division;
    }
}
