package org.alltiny.svg.parser;

/**
 * Is thrown when parsing encountered an error.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 06.12.2008
 */
public class SVGParseException extends Exception {

    public SVGParseException(String message) {
        super(message);
    }
}
