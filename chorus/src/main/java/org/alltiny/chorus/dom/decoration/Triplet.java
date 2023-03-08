package org.alltiny.chorus.dom.decoration;

/**
 * A bound binds two or more notes to legato.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 18:32:44
 */
public class Triplet extends Decoration<Triplet> {

    public enum Property {
        REF
    }

    public Triplet() {}

    public Triplet(int ref) {
        setRef(ref);
    }

    public int getRef() {
        return (int)get(Property.REF.name());
    }

    public Triplet setRef(int ref) {
        put(Property.REF.name(), ref);
        return this;
    }
}
