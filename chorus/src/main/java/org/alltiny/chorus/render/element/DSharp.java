package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.CubicCurve2D;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class DSharp extends Visual {

    private static final Shape SHARP = DSharp.createDSharp();

    public DSharp() {
        setPadding(new Rectangle2D.Float(-2,-2,4,4));
    }

    public void paintImpl(Graphics2D g) {
        g.fill(SHARP);
    }

    public Rectangle2D getBounds2D() {
        return SHARP.getBounds2D();
    }

    public static GeneralPath createDSharp() {
        GeneralPath sharp = new GeneralPath();
        // draw four rectangles as the corners of the double sharp.
        sharp.append(new Rectangle2D.Float(-0.50f * LINES_SPACE,-0.50f * LINES_SPACE,0.33f * LINES_SPACE,0.33f * LINES_SPACE), false);
        sharp.append(new Rectangle2D.Float(-0.50f * LINES_SPACE, 0.17f * LINES_SPACE,0.33f * LINES_SPACE,0.33f * LINES_SPACE), false);
        sharp.append(new Rectangle2D.Float(+0.17f * LINES_SPACE,-0.50f * LINES_SPACE,0.33f * LINES_SPACE,0.33f * LINES_SPACE), false);
        sharp.append(new Rectangle2D.Float(+0.17f * LINES_SPACE, 0.17f * LINES_SPACE,0.33f * LINES_SPACE,0.33f * LINES_SPACE), false);

        final float o = 0.5f; // outer offset.
        final float i = 0.2f; // inner offset.
        sharp.append(new CubicCurve2D.Float(-i * LINES_SPACE,-o * LINES_SPACE,-i * LINES_SPACE, 0, i * LINES_SPACE, 0, i * LINES_SPACE,-o * LINES_SPACE), false);
        sharp.append(new CubicCurve2D.Float(+o * LINES_SPACE,-i * LINES_SPACE, 0, -i * LINES_SPACE, 0, i * LINES_SPACE, o * LINES_SPACE, i * LINES_SPACE), true);
        sharp.append(new CubicCurve2D.Float(+i * LINES_SPACE, o * LINES_SPACE, i * LINES_SPACE, 0,-i * LINES_SPACE, 0, -i * LINES_SPACE, o * LINES_SPACE), true);
        sharp.append(new CubicCurve2D.Float(-o * LINES_SPACE, i * LINES_SPACE, 0, i * LINES_SPACE, 0, -i * LINES_SPACE,-o * LINES_SPACE,-i * LINES_SPACE), true);
        return sharp;
    }
}
