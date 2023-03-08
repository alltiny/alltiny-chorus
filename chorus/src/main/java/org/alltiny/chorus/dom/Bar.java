package org.alltiny.chorus.dom;

/**
 * Models a measure.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008
 */
public class Bar extends DurationElement<Bar> {

    public enum Property {
        KEEP_BEAT_DURATION,
        DISPLAY_STYLE
    }

    public Bar() {
        setKeepBeatDuration(false);
        setDisplayStyle(BarDisplayStyle.Fraction);
    }

    public boolean isKeepBeatDuration() {
        return (boolean)get(Property.KEEP_BEAT_DURATION.name());
    }

    public Bar setKeepBeatDuration(boolean keepBeatDuration) {
        put(Property.KEEP_BEAT_DURATION.name(), keepBeatDuration);
        return this;
    }

    public BarDisplayStyle getDisplayStyle() {
        return (BarDisplayStyle)get(Property.DISPLAY_STYLE.name());
    }

    public Bar setDisplayStyle(BarDisplayStyle displayStyle) {
        put(Property.DISPLAY_STYLE.name(), displayStyle);
        return this;
    }

    @Override
    protected void checkForDividability() {
        // don't adapt bars when they are dividable.
    }
}
