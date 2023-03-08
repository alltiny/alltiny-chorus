package org.alltiny.svg.parser.path;

import org.alltiny.svg.parser.SVGNumberParser;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.PushbackInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 05.12.2008
 */
public class SVGPathMoveToRelParser {

    /**
     * This method tries to read a pair of coordinates and adds it as relative
     * moveto-point to the given path.
     */
    public static void parseMoveToRel(GeneralPath path, PushbackInputStream stream) throws IOException {
        for (;;) { // forever
            String x = SVGNumberParser.parseNumberFromStream(stream);
            String y = SVGNumberParser.parseNumberFromStream(stream);

            // a valid pair of numbers found?
            if (x.length() > 0 && y.length() > 0) {
                Point2D last = path.getCurrentPoint();
                if (last == null) {
                    path.moveTo(Float.parseFloat(x), Float.parseFloat(y));
                } else {
                    path.moveTo((float)last.getX() + Float.parseFloat(x), (float)last.getY() + Float.parseFloat(y));
                }
            } else { // no - then break.
                break;
            }
        }
    }
}
