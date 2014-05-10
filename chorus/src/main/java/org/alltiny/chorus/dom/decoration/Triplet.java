package org.alltiny.chorus.dom.decoration;

/**
 * A bound binds two or more notes to legato.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 18:32:44
 */
public class Triplet extends Decoration {

    private int ref;

    public Triplet(int ref) {
        this.ref = ref;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }
}