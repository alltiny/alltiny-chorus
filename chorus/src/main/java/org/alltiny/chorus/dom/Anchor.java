package org.alltiny.chorus.dom;

/**
 * An anchor is an invisible element which may be used to serve as
 * reference where in the sequence another inline sequence may start.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 18:32:44
 */
public class Anchor extends Element<Anchor> {

    public enum Property {
        REF,
        /** the current tick count at this anchor. */
        TICK,
        /** the current velocity at this anchor. */
        VELOCITY
    }

    public int getRef() {
        return (int)get(Property.REF.name());
    }

    public Anchor setRef(int ref) {
        put(Property.REF.name(), ref);
        return this;
    }

    public long getTick() {
        return (long)get(Property.TICK.name());
    }

    public Anchor setTick(long tick) {
        put(Property.TICK.name(), tick);
        return this;
    }

    public int getVelocity() {
        return (int)get(Property.VELOCITY.name());
    }

    public Anchor setVelocity(int velocity) {
        put(Property.VELOCITY.name(), velocity);
        return this;
    }
}
