package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * This class represents the five lines onto which the notes are typically rendered.
 * This class was created to achieve a better performance by drawing the lines completely
 * from the beginning of the line to it's end.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 18.05.2010 18:02:00
 */
public class Lines extends Visual {

    public Lines() {
        super(0);
    }

    /**
     * This method draws the five lines from the position of the starting element
     * to the position of the ending element. If at least one of both is null, then
     * this method will draw nothing.
     */
    @Override
    public void paintImpl(Graphics2D g2d) {
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.transform(AffineTransform.getTranslateInstance(0, startElement.getAbsMedianY()));

        // draw the lines from the left border of the starting element to the right border of the ending element.
        final float stroke = 1f;
        //final double right = endElement.getAbsMedianX() + endElement.getExtendRight();
        final double width = getWidth();
        g2d.fill(new Rectangle2D.Double(0, -2 * LINES_SPACE - 0.5 * stroke, width, stroke));
        g2d.fill(new Rectangle2D.Double(0,     -LINES_SPACE - 0.5 * stroke, width, stroke));
        g2d.fill(new Rectangle2D.Double(0,                   -0.5 * stroke, width, stroke));
        g2d.fill(new Rectangle2D.Double(0,      LINES_SPACE - 0.5 * stroke, width, stroke));
        g2d.fill(new Rectangle2D.Double(0, +2 * LINES_SPACE - 0.5 * stroke, width, stroke));
    }
}