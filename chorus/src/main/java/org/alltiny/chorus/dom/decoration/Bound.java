package org.alltiny.chorus.dom.decoration;

/**
 * A bound binds two or more notes to legato.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 18:32:44
 */
public class Bound extends Decoration<Bound> {

    public enum Property {
        REF
    }

    public Bound() {}

    public Bound(int ref) {
        setRef(ref);
    }

    public int getRef() {
        return (int)get(Property.REF.name());
    }

    public Bound setRef(int ref) {
        put(Property.REF.name(), ref);
        return this;
    }
}
