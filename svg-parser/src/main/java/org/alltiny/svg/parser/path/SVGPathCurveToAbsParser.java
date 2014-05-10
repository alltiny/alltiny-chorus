package org.alltiny.svg.parser.path;

import org.alltiny.svg.parser.SVGNumberParser;

import java.awt.geom.GeneralPath;
import java.io.PushbackInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 05.12.2008
 */
public class SVGPathCurveToAbsParser {

    /**
     * This method tries to read pairs of coordinates and add each of them,
     * to the {@param path} as moveto-point. This method stops until no further
     * number pair could be recieved.
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
                path.curveTo(Float.parseFloat(x1), Float.parseFloat(y1),Float.parseFloat(x2), Float.parseFloat(y2),Float.parseFloat(x3), Float.parseFloat(y3));
            }
        }
    }
}
