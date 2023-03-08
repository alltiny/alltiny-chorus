package org.alltiny.chorus.dom;

/**
 * Models the end of a repetition section.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008
 */
public class RepeatEnd extends Element<RepeatEnd> {

    public enum Property {
        REF
    }

    public int getRef() {
        return (int)get(Property.REF.name());
    }

    public RepeatEnd setRef(int ref) {
        put(Property.REF.name(), ref);
        return this;
    }
}
