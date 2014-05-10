package org.alltiny.chorus.dom;

/**
 * This class represents an absolute tempo definition like
 * "66 quarter note per minute".
 * A tempo factor is used to define temporal changes on a bar line.
 * A tempo factor can be defined like: "3/4=1/2" which means the same
 * like "a dotted half note has the same duration like a half note".
 */
public class AbsoluteTempoChange extends Element {

    private DurationElement note;
    private int numberPerMinute;

    public DurationElement getNote() {
        return note;
    }

    /**
     * Sets the note type.
     */
    public void setNote(DurationElement note) {
        this.note = note;
    }

    public int getNumberPerMinute() {
        return numberPerMinute;
    }

    /**
     * Sets how many {@link #note}s shall be played in on minute.
     */
    public void setNumberPerMinute(int numberPerMinute) {
        this.numberPerMinute = numberPerMinute;
    }
}