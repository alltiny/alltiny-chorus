package org.alltiny.chorus.dom.decoration;

/**
 * A beam connects two or more notes with a bar, which replaces also the the flags.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 18:32:44
 */
public class Beam extends Decoration<Beam> {

    public enum Property {
        REF
    }

    public Beam() {}

    public Beam(int ref) {
        setRef(ref);
    }

    public int getRef() {
        return (int)get(Property.REF.name());
    }

    public Beam setRef(int ref) {
        put(Property.REF.name(), ref);
        return this;
    }
}
