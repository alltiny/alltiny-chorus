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
public class Sharp extends Visual {

    private static final Shape SHARP = createSharp();

    public Sharp() {
        setPadding(new Rectangle2D.Float(-2,-2,4,4));
    }

    public void paintImpl(Graphics2D g) {
        g.fill(SHARP);
    }

    public Rectangle2D getBounds2D() {
        return SHARP.getBounds2D();
    }

    public static GeneralPath createSharp() {
        GeneralPath sharp = new GeneralPath();
        sharp.append(new Rectangle2D.Float(-0.5f  * LINES_SPACE,-0.7f * LINES_SPACE,LINES_SPACE,0.4f * LINES_SPACE), false);
        sharp.append(new Rectangle2D.Float(-0.5f  * LINES_SPACE, 0.3f * LINES_SPACE,LINES_SPACE,0.4f * LINES_SPACE), false);
        sharp.append(new Rectangle2D.Float(-0.31f * LINES_SPACE,-1.33f * LINES_SPACE,0.125f * LINES_SPACE,2.66f * LINES_SPACE), false);
        sharp.append(new Rectangle2D.Float(+0.19f * LINES_SPACE,-1.33f * LINES_SPACE,0.125f * LINES_SPACE,2.66f * LINES_SPACE), false);
        sharp.transform(AffineTransform.getShearInstance(0, -0.3));
        return sharp;
    }
}
