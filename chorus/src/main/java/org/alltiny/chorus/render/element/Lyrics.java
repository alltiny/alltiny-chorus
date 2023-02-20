package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class Lyrics extends Visual {

    private static final int FONT_SIZE = 14;

    private final String text;
    private final Rectangle2D bounds;


    public Lyrics(final String text) {
        setPadding(new Rectangle2D.Float(-(int)(FONT_SIZE * 0.5),0, FONT_SIZE, (int)(FONT_SIZE * 1.5)));
        this.text = text;

        Rectangle2D metric = new Font("Verdana", Font.PLAIN, FONT_SIZE)
            .getStringBounds(this.text, new FontRenderContext(null, true, true));
        // shift the bounds to center the text.
        bounds = new Rectangle2D.Double(-0.5 * metric.getWidth(), metric.getMinY(), metric.getWidth(), metric.getHeight());
    }

    public void paintImpl(Graphics2D g) {
        g.setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
        g.drawString(text, (float)bounds.getMinX(), 0f);
    }

    public Rectangle2D getBounds2D() {
        return bounds;
    }
}
