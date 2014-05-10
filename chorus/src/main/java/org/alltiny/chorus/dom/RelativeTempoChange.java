package org.alltiny.chorus.dom;

/**
 * A tempo factor is used to define temporal changes on a bar line.
 * A tempo factor can be defined like: "3/4=1/2" which means the same
 * like "a dotted half note has the same duration like a half note".
 */
public class RelativeTempoChange extends Element {

    private DurationElement left;
    private DurationElement right;

    public DurationElement getLeft() {
        return left;
    }

    public RelativeTempoChange setLeft(DurationElement left) {
        this.left = left;
        return this;
    }

    public DurationElement getRight() {
        return right;
    }

    public RelativeTempoChange setRight(DurationElement right) {
        this.right = right;
        return this;
    }

    public double getDurationFactor() {
        return (double)left.getDuration() / left.getDivision() * right.getDivision() / right.getDuration();
    }
}
