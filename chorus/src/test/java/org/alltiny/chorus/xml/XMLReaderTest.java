package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Song;
import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensure that the {@link XMLReader} is working correctly.
 */
public class XMLReaderTest {

    @Test
    public void testParsingExample() throws Exception {
        Song song = XMLReader.readSongFromXML(getClass().getClassLoader().getResourceAsStream("example.xml"));
        Assert.assertNotNull("song should not be null", song);
        Assert.assertEquals("song title", "Brich an, o schönes Morgenlicht", song.getTitle());
    }
}
