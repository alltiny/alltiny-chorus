package org.alltiny.chorus.render;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * This class is the super class of all visual components. A visual is drawn
 * relative to it's median pos ({@link #absMedianX}, {@link #absMedianY}}).
 * The {@link LayoutManager} implementation of the container holding the visual
 * is responsible for:
 *  - determining ({@link #absMedianX}, {@link #absMedianY}}) the position
 *    where to render this visual.
 *  - determining the dimensions to left, right, top and bottom which this
 *    visual has to fill.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 05.02.2009 16:39:18
 */
public abstract class Visual extends Component {

    public static final String ABS_MEDIAN_X = "absMedianX";
    public static final String ABS_MEDIAN_Y = "absMedianY";

    public static final int LINES_SPACE = 9;
    public static final float DOT_SIZE = 3;
    public static final boolean DRAW_BOUNDING_BOX = false;
    public static final boolean DRAW_MEDIAN_LINE = false;

    // this is the median point where this visual shall be rendered.
    protected double absMedianX;
    protected double absMedianY;
    // this dimensions are relative to the median point.
    protected double extendLeft;
    protected double extendRight;
    protected double extendTop;
    protected double extendBottom;

    /* the padding defines how much space shall exist around the bounds of this
     * visual at minimum. */
    private Rectangle2D padding;

    protected Visual() {
        this(10);
    }

    protected Visual(int hPadding) {
        this.padding = new Rectangle2D.Double(-hPadding, 0, 2 * hPadding, 0);
    }

    /**
     * This method returns the bounds of the implementing element relative to
     * x and y. Rectangle's x and y coordinate may be the offset to the left
     * and upper bound relative to the x and y coordinates of this element. The
     * rectangle's width and height should be the width and height of this
     * element.
     * The element's offset is necessary to achieve that all elements in the
     * same column can be arranged like draw on the same vertical line.
     */
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Float(0, 0, 0, 0);
    }

    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics.create();
        g.transform(AffineTransform.getTranslateInstance(absMedianX, absMedianY));
        // check if this visual must be painted.
        if (!g.hitClip(getX(), getY(), getWidth(), getHeight())) {
            //g.transform(AffineTransform.getTranslateInstance(absMedianX, absMedianY));
            if (DRAW_MEDIAN_LINE) {
                // draw a line to show where the center is.
                Graphics2D g2d = (Graphics2D)g.create();
                g2d.setColor(Color.green);
                g2d.drawLine(0,-2 * LINES_SPACE,0,2 * LINES_SPACE);
            }
            if (DRAW_BOUNDING_BOX) {
                // draw the bounding box to visualize.
                Graphics2D g2d = (Graphics2D)g.create();
                g2d.setColor(Color.red);
                g2d.draw(getBounds2D());
            }
            paintImpl(g);
        }
    }

    protected abstract void paintImpl(Graphics2D g2d);

    /**
     * @return this (fluent interface pattern)
     */
    public Visual setAbsMedianX(double x) {
        double old = absMedianX;
        absMedianX = x;
        firePropertyChange(ABS_MEDIAN_X, old, x);
        return this; // fluent interface support.
    }

    /**
     * @return this (fluent interface pattern)
     */
    public Visual setAbsMedianY(double y) {
        double old = absMedianY;
        absMedianY = y;
        firePropertyChange(ABS_MEDIAN_Y, old, y);
        return this; // fluent interface support.
    }

    public double getAbsMedianX() {
        return absMedianX;
    }

    public double getAbsMedianY() {
        return absMedianY;
    }

    /**
     * @return this (fluent interface pattern)
     */
    public Visual setPadding(Rectangle2D padding) {
        this.padding = padding;
        return this; // fluent interface support.
    }

    public Rectangle2D getPadding() {
        return padding;
    }

    public double getExtendLeft() {
        return extendLeft;
    }

    public void setExtendLeft(double extendLeft) {
        this.extendLeft = extendLeft;
    }

    public double getExtendRight() {
        return extendRight;
    }

    public void setExtendRight(double extendRight) {
        this.extendRight = extendRight;
    }

    public double getExtendTop() {
        return extendTop;
    }

    public void setExtendTop(double extendTop) {
        this.extendTop = extendTop;
    }

    public double getExtendBottom() {
        return extendBottom;
    }

    public void setExtendBottom(double extendBottom) {
        this.extendBottom = extendBottom;
    }
}
