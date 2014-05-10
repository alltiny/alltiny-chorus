package org.alltiny.chorus.dom.decoration;

/**
 * A beam connects two or more notes with a bar, which replaces also the the flags.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 18:32:44
 */
public class Beam extends Decoration {

    private int ref;

    public Beam(int ref) {
        this.ref = ref;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }
}
