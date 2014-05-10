package org.alltiny.chorus.gui.canvas;

import org.alltiny.chorus.dom.Key;
import org.alltiny.chorus.dom.BaseNote;
import org.alltiny.chorus.dom.decoration.AccidentalSign;

import java.util.HashMap;

/**
 * This class was created to implement the accidental sign behavior in which
 * accidental sign are lasting till the end of the beat or until they are
 * overwritten by another accidental sign.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.03.2010 17:14:12
 */
/*package-local*/ class KeyMapHelper {

    private final Key base;
    private HashMap<Integer,HashMap<BaseNote,AccidentalSign>> currentMapping = new HashMap<Integer,HashMap<BaseNote,AccidentalSign>>();

    public KeyMapHelper(Key base) {
        this.base = base;
    }

    /**
     * This method returns the current accidental modification for the given
     * note in the given octave. If none modification exists then the base key
     * set is used.
     *
     * @param octave as int
     * @param note base note for which the current modification should be return.
     */
    public AccidentalSign getAccidentalSign(int octave, BaseNote note) {
        // check if a modification exists.
        if (currentMapping.containsKey(octave) && currentMapping.get(octave).containsKey(note)) {
            return currentMapping.get(octave).get(note);
        } else { // return the base modification if no current modification exists.
            return base.getMod(note);
        }
    }

    /**
     * This method registers the current note'S modification to the mapping.
     */
    public void setAccidentalSign(int octave, BaseNote note, AccidentalSign sign) {
        if (!currentMapping.containsKey(octave)) {
            currentMapping.put(octave, new HashMap<BaseNote,AccidentalSign>());
        }
        currentMapping.get(octave).put(note, sign);
    }

    /**
     * At the end of a beat all modifications shall be removed. This clear
     * method does the job for you.
     */
    public void clear() {
        currentMapping.clear();
    }
}
