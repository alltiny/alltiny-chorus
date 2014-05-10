package org.alltiny.svg.parser;

import org.alltiny.svg.parser.path.*;

import java.awt.geom.GeneralPath;
import java.io.PushbackInputStream;
import java.io.IOException;

/**
 * This parser parses a svg-path.
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 05.12.2008
 */
public class SVGPathParser {

    public static GeneralPath parsePath(PushbackInputStream stream) throws IOException,SVGParseException {
        GeneralPath path = new GeneralPath();

        for (int chr = stream.read(); chr != -1; chr = stream.read()) {
            if (chr == ' ') {
                continue; // skip white-spaces.
            } else if (chr == 'M') { // absolute move to command.
                SVGPathMoveToAbsParser.parseMoveToAbs(path, stream);
            } else if (chr == 'm') { // relative move to command
                SVGPathMoveToRelParser.parseMoveToRel(path, stream);
            } else if (chr == 'C') {
                SVGPathCurveToAbsParser.parse(path, stream);
            } else if (chr == 'c') {
                SVGPathCurveToRelParser.parse(path, stream);
            } else if (chr == 'L') { // absolute line to command.
                SVGPathLineToAbsParser.parseLineToAbs(path, stream);
            } else if (chr == 'l') { // relative line to command.
                SVGPathLineToRelParser.parseLineToRel(path, stream);
            } else if (chr == 'Z' || chr == 'z') {
                path.closePath();
                return path;
            } else {
                throw new SVGParseException("Unknown path control character \'" + (char)chr + "\' (" + chr + ") found");
            }
        }
        return path;
    }
}
