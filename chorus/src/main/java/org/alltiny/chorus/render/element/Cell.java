package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;

import java.util.ArrayList;
import java.awt.geom.Rectangle2D;
import java.awt.*;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 10.01.2010 11:02:15
 */
public class Cell extends Visual {

    private ArrayList<Visual> notes  = new ArrayList<Visual>();

    private Rectangle2D bounds = new Rectangle2D.Float(0, 0, 0, 0);

    public Cell() {
        super(0);
    }

    public Cell addVisualToNotes(Visual element) {
        notes.add(element);
        update();
        return this; // fluent interface support.
    }

    protected void update() {
        // reset
        bounds.setRect(0, 0, 0, 0);
        // recalculate
        for (Visual element : notes) {
            Rectangle2D box = element.getBounds2D();
            Rectangle2D pad = element.getPadding();

            double minX = Math.min(box.getMinX() + pad.getMinX(), bounds.getMinX());
            double minY = Math.min(box.getMinY() + pad.getMinY(), bounds.getMinY());
            double maxX = Math.max(box.getMaxX() + pad.getMaxX(), bounds.getMaxX());
            double maxY = Math.max(box.getMaxY() + pad.getMaxY(), bounds.getMaxY());

            bounds.setRect(minX, minY, maxX - minX, maxY - minY);
        }
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D)graphics.create();
        super.paint(g2d);

        for (Visual element : notes) {
            // copy the original graphics context.
            element.paint(graphics.create());
        }
    }

    @Override
    protected void paintImpl(Graphics2D g2d) {}

    /**
     * This method is overwritten from {@link Visual} to pass the absolute
     * median x position to the elements of this cell.
     */
    @Override
    public Visual setAbsMedianX(double x) {
        for (Visual element : notes) {
            element.setAbsMedianX(x);
        }
        return super.setAbsMedianX(x);
    }

    /**
     * This method is overwritten from {@link Visual} to pass the absolute
     * median y position to the elements of this cell.
     */
    @Override
    public Visual setAbsMedianY(double y) {
        for (Visual element : notes) {
            element.setAbsMedianY(y);
        }
        return super.setAbsMedianY(y);
    }

    public Rectangle2D getBounds2D() {
        return bounds;
    }
}
