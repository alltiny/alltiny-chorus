package org.alltiny.chorus.dom;

/**
 * This class represents an absolute tempo definition like
 * "66 quarter notes per minute".
 *
 * See also {@link RelativeTempoChange} for relative tempo changes.
 */
public class AbsoluteTempoChange extends Element<AbsoluteTempoChange> {

    public enum Property {
        NOTE,
        NUMBER_PER_MINUTE
    }

    public DurationElement<?> getNote() {
        return (DurationElement<?>)get(Property.NOTE.name());
    }

    /**
     * Sets the note type.
     */
    public AbsoluteTempoChange setNote(DurationElement<?> note) {
        put(Property.NOTE.name(), note);
        return this;
    }

    public int getNumberPerMinute() {
        return (int)get(Property.NUMBER_PER_MINUTE.name());
    }

    /**
     * Sets how many notes shall be played in a minute.
     */
    public AbsoluteTempoChange setNumberPerMinute(int numberPerMinute) {
        put(Property.NUMBER_PER_MINUTE.name(), numberPerMinute);
        return this;
    }
}
