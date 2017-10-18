package org.alltiny.svg.parser;

import org.junit.Assert;
import org.junit.Test;

import java.awt.geom.GeneralPath;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 * This test ensures that the {@link SVGPathParser} is working.
 */
public class SVGPathParserTest {

    @Test
    public void testParsingPathWithRelativeLine() throws IOException, SVGParseException {
        PushbackInputStream stream = new PushbackInputStream(new ByteArrayInputStream("m 6.67446,-2.59352 l 4.12913,-10e-6 -0.20997,21.99911".getBytes()));
        GeneralPath path = SVGPathParser.parsePath(stream);
        Assert.assertNotNull("path should not be null", path);
    }
}
