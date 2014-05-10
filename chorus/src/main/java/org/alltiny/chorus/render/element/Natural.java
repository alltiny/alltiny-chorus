package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class Natural extends Visual {

    private static final Shape NATURAL = Natural.createNatural();

    public Natural() {
        setPadding(new Rectangle2D.Float(-2,-2,4,4));
    }

    public void paintImpl(Graphics2D g) {
        g.fill(NATURAL);
    }

    public Rectangle2D getBounds2D() {
        return NATURAL.getBounds2D();
    }

    public static GeneralPath createNatural() {
        GeneralPath path = new GeneralPath();
        path.append(new Rectangle2D.Float(-0.25f * LINES_SPACE,-0.7f * LINES_SPACE,0.500f * LINES_SPACE,0.4f * LINES_SPACE), false);
        path.append(new Rectangle2D.Float(-0.25f * LINES_SPACE, 0.3f * LINES_SPACE,0.500f * LINES_SPACE,0.4f * LINES_SPACE), false);
        path.append(new Rectangle2D.Float(-0.31f * LINES_SPACE,-1.6f * LINES_SPACE,0.125f * LINES_SPACE,2.3f * LINES_SPACE), false);
        path.append(new Rectangle2D.Float(+0.19f * LINES_SPACE,-0.7f * LINES_SPACE,0.125f * LINES_SPACE,2.3f * LINES_SPACE), false);
        path.transform(AffineTransform.getShearInstance(0, -0.3));
        return path;
    }
}
