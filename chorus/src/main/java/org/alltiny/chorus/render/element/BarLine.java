package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 05.02.2009 16:36:20
 */
public class BarLine extends Visual {

    private static final float LINE_WEIGHT = 1;
    private final ArrayList<Shape> bar = new ArrayList<Shape>();
    private Rectangle2D bounds;

    public BarLine() {
        setPadding(new Rectangle2D.Float(-10,0,20,0));
        update();
    }

    private void update() {
        bar.clear();
        bar.add(new Rectangle2D.Double(-0.5 * LINE_WEIGHT, -2 * LINES_SPACE, 1, 4 * LINES_SPACE));

        // precompute the bound of this bar.
        bounds = (bar.size() > 0) ? bar.get(0).getBounds2D() : new Rectangle2D.Float(0,0,0,0);
        for (Shape shape : bar) {
            bounds.add(shape.getBounds2D());
        }
    }

    public Rectangle2D getBounds2D() {
        return bounds;
    }

    public void paintImpl(Graphics2D g) {
        for (Shape shape : bar) {
            g.fill(shape);
        }
    }
}
