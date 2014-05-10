package org.alltiny.svg.parser.path;

import org.alltiny.svg.parser.SVGNumberParser;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 * This parser parses a relative cubic Bezier curve.
 */
public class SVGPathCurveToRelParser {

    /**
     * This method tries to read pairs of coordinates and add each of them,
     * to the {@param path} as moveto-point. This method stops until no further
     * number pair could be received.
     */
    public static void parse(GeneralPath path, PushbackInputStream stream) throws IOException {
        for (;;) { // for ever
            String x1 = SVGNumberParser.parseNumberFromStream(stream);
            String y1 = SVGNumberParser.parseNumberFromStream(stream);
            String x2 = SVGNumberParser.parseNumberFromStream(stream);
            String y2 = SVGNumberParser.parseNumberFromStream(stream);
            String x3 = SVGNumberParser.parseNumberFromStream(stream);
            String y3 = SVGNumberParser.parseNumberFromStream(stream);

            // break if any of the requested coordinates is not defined.
            if (x1.length() == 0 || y1.length() == 0 || x2.length() == 0 || y2.length() == 0 || x3.length() == 0 || y3.length() == 0) {
                break;
            } else {
                Point2D last = path.getCurrentPoint();
                if (last == null) {
                    last = new Point2D.Float(0, 0);
                }
                float p1x = Float.parseFloat(x1) + (float)last.getX();
                float p1y = Float.parseFloat(y1) + (float)last.getY();
                float p2x = Float.parseFloat(x2) + (float)last.getX();
                float p2y = Float.parseFloat(y2) + (float)last.getY();
                float p3x = Float.parseFloat(x3) + (float)last.getX();
                float p3y = Float.parseFloat(y3) + (float)last.getY();
                path.curveTo(p1x, p1y, p2x, p2y, p3x, p3y);
            }
        }
    }
}