package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;
import org.alltiny.svg.parser.SVGPathParser;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class Flat extends Visual {

    private static final Shape FLAT = Flat.createFlat();

    public Flat() {
        setPadding(new Rectangle2D.Float(-2,-2,4,4));
    }

    public void paintImpl(Graphics2D g) {
        g.fill(FLAT);
    }

    public Rectangle2D getBounds2D() {
        return FLAT.getBounds2D();
    }

    public static GeneralPath createFlat() {
        try {
            GeneralPath flat = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 95.51,442.569 L 95.51,435.29733 L 94.947,435.29733 L 94.947,447.75213 C 94.954863,448.45985 95.327293,448.27517 95.69,448.06413 C 97.919221,446.78973 99.97807,445.38686 99.978,443.179 C 99.978874,440.70755 97.092912,440.77091 95.51,442.569 M 95.51,447.28013 L 95.51,443.848 C 95.995018,442.49711 97.794317,441.86392 98.142,443.393 C 98.380997,444.87734 96.505615,446.50336 95.51,447.28013 z".getBytes())));
            flat.transform(AffineTransform.getTranslateInstance(-97,-444.5));
            flat.transform(AffineTransform.getScaleInstance(1.4,1.4));
            return flat;
        } catch (Exception ex) {
            throw new Error("SVG could not be parsed");
        }
    }
}
