package org.alltiny.svg.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 * This test ensures that the {@link SVGNumberParser} is working.
 */
public class SVGNumberParserTest {

    @Test
    public void testLeadingWhiteSpacesAreIgnored() throws IOException {
        PushbackInputStream stream = new PushbackInputStream(new ByteArrayInputStream("  1".getBytes()));
        Assert.assertEquals("number in '  1' not correct", "1", SVGNumberParser.parseNumberFromStream(stream));
    }

    @Test
    public void parseNumbersSeparatedByMinus() throws IOException {
        PushbackInputStream stream = new PushbackInputStream(new ByteArrayInputStream(" 100-2".getBytes()));
        Assert.assertEquals("first number in ' 100-2' not correct", "100", SVGNumberParser.parseNumberFromStream(stream));
        Assert.assertEquals("second number in ' 100-2' not correct", "-2", SVGNumberParser.parseNumberFromStream(stream));
    }

    @Test
    public void parseNumbersSeparatedByDot() throws IOException {
        PushbackInputStream stream = new PushbackInputStream(new ByteArrayInputStream("0.45.3".getBytes()));
        Assert.assertEquals("first number in ' 0.45.3' not correct", "0.45", SVGNumberParser.parseNumberFromStream(stream));
        Assert.assertEquals("second number in ' 0.45.3' not correct", ".3", SVGNumberParser.parseNumberFromStream(stream));
    }

    @Test
    public void parseNumbersSeparatedByComma() throws IOException {
        PushbackInputStream stream = new PushbackInputStream(new ByteArrayInputStream("45,2".getBytes()));
        Assert.assertEquals("first number in '45,2' not correct", "45", SVGNumberParser.parseNumberFromStream(stream));
        Assert.assertEquals("second number in '45,2' not correct", "2", SVGNumberParser.parseNumberFromStream(stream));
    }

    @Test
    public void parseNumberWithExponentSmall() throws IOException {
        PushbackInputStream stream = new PushbackInputStream(new ByteArrayInputStream("-1e-5".getBytes()));
        Assert.assertEquals("number in '-1e-5' not correct", "-1e-5", SVGNumberParser.parseNumberFromStream(stream));
    }

    @Test
    public void parseNumberWithExponentLarge() throws IOException {
        PushbackInputStream stream = new PushbackInputStream(new ByteArrayInputStream("-1E-5".getBytes()));
        Assert.assertEquals("number in '-1E-5' not correct", "-1E-5", SVGNumberParser.parseNumberFromStream(stream));
    }

    @Test
    public void parseNumberWithExponentWithTrailingWhiteSpace() throws IOException {
        PushbackInputStream stream = new PushbackInputStream(new ByteArrayInputStream("-10e-6 ".getBytes()));
        Assert.assertEquals("number in '-10e-6' not correct", "-10e-6", SVGNumberParser.parseNumberFromStream(stream));
    }
}
