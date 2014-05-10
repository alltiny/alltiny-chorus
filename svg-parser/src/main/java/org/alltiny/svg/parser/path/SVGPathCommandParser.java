package org.alltiny.svg.parser.path;

import java.awt.geom.GeneralPath;
import java.io.PushbackInputStream;
import java.io.IOException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 25.04.2009
 */
public interface SVGPathCommandParser {

    public void parseMoveToAbs(GeneralPath path, PushbackInputStream stream) throws IOException;
}
