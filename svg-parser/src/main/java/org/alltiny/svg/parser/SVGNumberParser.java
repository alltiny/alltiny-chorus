package org.alltiny.svg.parser;

import java.io.PushbackInputStream;
import java.io.IOException;

/**
 * This parser parses a number from a svg-stream.
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 05.12.2008
 */
public class SVGNumberParser {

    /**
     * This method tries to read the next appearing characters in stream as a
     * svg number. any leading white-spaces will be ignored. This method returns
     * the found number as String to let the calling instance define how to
     * parse the found number as number (long, float, double, BigDecimal).
     *
     * Please {@see http://www.w3.org/TR/SVG/paths.html} how svg defines numbers
     * in a stream. SVG does only allow digits [0..9], a point '.' and presigns
     * '+' or '-' as a number. If any not allowed character is found, then the
     * number is finished. Examples of two numbers:
     *
     * "100-200" is 100 and -200
     * "0.45.3" is 0.45 and .3
     */
    public static String parseNumberFromStream(PushbackInputStream stream) throws IOException {
        StringBuffer buf = new StringBuffer();

        int chr = stream.read();
        // read and drop leading white-spaces.
        while (chr != -1 && chr == ' ') {
            chr = stream.read();
        }

        if (chr != -1) {
            stream.unread(chr);
        }

        // parse a fractional number form the stream.
        buf.append(parseFractionalFromStream(stream));

        // look for and exponent.
        chr = stream.read();
        if (chr == 'e' || chr == 'E') {
            buf.append((char)chr);
            // parse the exponent.
            buf.append(parseFractionalFromStream(stream));
        }

        // clean up trailing ',' or white-spaces.
        while (chr != -1 && (chr == ' ' || chr == ',')) {
            chr = stream.read();
        }

        // push back the last read character unless it is EOS.
        if (chr != -1) {
            stream.unread(chr);
        }

        return buf.toString();
    }

    /**
     * This method will read a fractional from the given stream.
     */
    private static String parseFractionalFromStream(PushbackInputStream stream) throws IOException {
        StringBuffer buf = new StringBuffer();

        int chr = stream.read();

        // a fractional may start with a sign '+' or '-'.
        if (chr == '+' || chr == '-') {
            buf.append((char)chr);
            chr = stream.read();
        }

        /* a fraction may contain non or one point. if a second point appears, then
         * a second number is starting. example: "1.67.3" is "1.67" and "0.3" */
        boolean pointFound = false;
        // read as long valid characters of a number are read.
        while (chr != -1 && ((chr >= '0' && chr <= '9') || (chr == '.' && !pointFound))) {
            if (chr == '.') {
                pointFound = true;
            }
            buf.append((char)chr);
            chr = stream.read();
        }

        // push back the last read character unless it is EOS.
        if (chr != -1) {
            stream.unread(chr);
        }

        return buf.toString();
    }
}
