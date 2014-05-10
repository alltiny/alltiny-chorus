package org.alltiny.chorus.dom;

import java.util.ArrayList;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008 20:13:43
 */
public class InlineSequence extends ArrayList<Element> {

    /** This is the anchor where this inline sequence will start. not null. */
    private Anchor anchor;

    public InlineSequence(Anchor anchor) {
        this.anchor = anchor;
    }

    public void addElement(Element element) {
        add(element);
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }
}
