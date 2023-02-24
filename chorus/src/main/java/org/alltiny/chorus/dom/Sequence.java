package org.alltiny.chorus.dom;

import org.alltiny.chorus.base.type.Clef;
import org.alltiny.chorus.base.type.Key;

import java.util.ArrayList;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008 20:13:43
 */
public class Sequence extends ArrayList<Element> {

    private Clef clef;
    private Key key;

    public Sequence(Clef clef) {
        this.clef = clef;
    }

    public void addElement(Element element) {
        add(element);
        element.setSequence(this);
    }

    public Clef getClef() {
        return clef;
    }

    public void setClef(Clef clef) {
        this.clef = clef;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
