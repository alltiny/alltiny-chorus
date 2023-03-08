package org.alltiny.chorus.dom;

/**
 * A tempo factor is used to define temporal changes on a bar line.
 * A tempo factor can be defined like: "3/4=1/2" which means the same
 * as "a dotted half note has the same duration as a half note".
 *
 * See also {@link AbsoluteTempoChange} for absolute tempo changes.
 */
public class RelativeTempoChange extends Element<RelativeTempoChange> {

    public enum Property {
        LEFT,
        RIGHT
    }

    public DurationElement<?> getLeft() {
        return (DurationElement<?>)get(Property.LEFT.name());
    }

    public RelativeTempoChange setLeft(DurationElement<?> left) {
        put(Property.LEFT.name(), left);
        return this;
    }

    public DurationElement<?> getRight() {
        return (DurationElement<?>)get(Property.RIGHT.name());
    }

    public RelativeTempoChange setRight(DurationElement<?> right) {
        put(Property.RIGHT.name(), right);
        return this;
    }

    public double getDurationFactor() {
        return (double)getLeft().getDuration() / getLeft().getDivision() * getRight().getDivision() / getRight().getDuration();
    }
}
