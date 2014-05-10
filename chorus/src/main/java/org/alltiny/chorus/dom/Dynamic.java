package org.alltiny.chorus.dom;

import java.util.HashMap;

/**
 * This class stores the mapping of dynamic symbols like "pp", "mp", "mf", "f"
 * to the velocity values 0..127
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 23.09.2009 17:45:20
 */
public class Dynamic {

    private final HashMap<String,Integer> values;

    public Dynamic() {
        values = new HashMap<String,Integer>();
    }

    public void addValue(String key, int value) {
        if (value < 0 || value > 127) {
            throw new IllegalArgumentException("Value has to be a 7 Bit value.");
        }
        values.put(key, value);
    }

    public int getValue(String key) {
        return values.get(key);
    }
}
