package org.alltiny.chorus.dom;

/**
 * An anchor is an invisible element which may be used to serve as
 * reference where in the sequence another inline sequence may start.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 24.11.2008 18:32:44
 */
public class Anchor extends Element {

    private int ref;
    /** the tick count at this anchor. */
    private long tick;
    /** the current velocity at this anchor. */
    private int velocity;

    public Anchor(int ref) {
        this.ref = ref;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
