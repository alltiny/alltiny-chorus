package org.alltiny.svg.parser.path;

import org.alltiny.svg.parser.SVGNumberParser;

import java.awt.geom.GeneralPath;
import java.io.PushbackInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 05.12.2008
 */
public class SVGPathLineToAbsParser {

    /**
     * This method tries to read pairs of coordinates and add each of them,
     * to the {@param path} as moveto-point. This method stops until no further
     * number pair could be received.
     */
    public static void parseLineToAbs(GeneralPath path, PushbackInputStream stream) throws IOException {
        for (;;) { // forever
            String x = SVGNumberParser.parseNumberFromStream(stream);
            String y = SVGNumberParser.parseNumberFromStream(stream);

            // a valid pair of numbers found?
            if (x.length() > 0 && y.length() > 0) {
                path.lineTo(Float.parseFloat(x), Float.parseFloat(y));
            } else { // no - then break.
                break;
            }
        }
    }
}
