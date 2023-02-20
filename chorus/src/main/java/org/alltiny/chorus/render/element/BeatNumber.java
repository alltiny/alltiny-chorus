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
public class BeatNumber extends Visual {

    private final String number;
    private final Rectangle2D bounds;

    public BeatNumber(final int number) {
        setPadding(new Rectangle2D.Float(0,-2,0,4));
        this.number = String.valueOf(number);

        bounds = new Font("Verdana", Font.ITALIC, 10)
            .getStringBounds(this.number, new FontRenderContext(null, true, true));
    }

    public void paintImpl(Graphics2D g) {
        g.setFont(new Font("Verdana", Font.ITALIC, 10));
        g.drawString(number, 0f, 0f);
    }

    public Rectangle2D getBounds2D() {
        return bounds;
    }
}
