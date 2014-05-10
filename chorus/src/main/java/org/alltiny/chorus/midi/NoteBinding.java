package org.alltiny.chorus.midi;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 02.07.2009 22:14:09
 */
public class NoteBinding {

    private int id;
    private int noteValue;

    public NoteBinding(int id, int noteValue) {
        this.id = id;
        this.noteValue = noteValue;
    }

    public int getId() {
        return id;
    }

    public int getNoteValue() {
        return noteValue;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final NoteBinding that = (NoteBinding) o;

        if (id != that.id) {
            return false;
        }
        if (noteValue != that.noteValue) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = id;
        result = 29 * result + noteValue;
        return result;
    }
}
