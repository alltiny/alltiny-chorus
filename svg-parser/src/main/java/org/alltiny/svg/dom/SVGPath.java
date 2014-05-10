package org.alltiny.svg.dom;

import java.awt.*;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.04.2009
 */
public class SVGPath {

    private Shape path;

    public SVGPath(Shape path) {
        this.path = path;
    }

    public void paint(Graphics2D graphics) {
        Graphics2D g = (Graphics2D)graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fill(path);
    }

    public Rectangle getBounds() {
        return path.getBounds();
    }
}
