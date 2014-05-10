package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.DurationElement;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 14.01.2008 22:41:01
 */
public class XMLDurationElementFactory {

    /** declared contructor as private to avoid instantiation of this class. */
    private XMLDurationElementFactory() {}

    /**
     * This method creats a {@link DurationElement} object from the given attribute
     * <code>duration</code>.
     *
     * @param duration of the element
     */
    public static DurationElement createDurationElement(String duration) {
        if (duration == null) {
            throw new NullPointerException("duration of DurationElement can not be null.");
        }

        int pos = duration.indexOf('/');
        if (pos < 0) { // if no divisor is given, then assume the divisor as 1.
            return new DurationElement(Integer.parseInt(duration), 1);
        } else {
            String prefix = duration.substring(0, pos);
            String suffix = duration.substring(pos, duration.length() - 1);
            int dur = 1, div = 1;

            if (prefix.length() > 0) {
                dur = Integer.parseInt(prefix);
            }
            if (suffix.length() > 0) {
                div = Integer.parseInt(suffix);
            }

            return new DurationElement(dur, div);
        }
    }
}
