package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.alltiny.chorus.base.type.AccidentalSign;
import org.alltiny.chorus.base.type.BaseNote;
import org.alltiny.chorus.base.type.NoteValue;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

public class XMLSongV1Test {

    @Test
    public void testUnmarshallingRandomProperties() throws JAXBException {
        final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<song>\n" +
            "<title>Cantate Domino</title>\n" +
            "<author>Claudio Monteverdi</author>\n" +
            "<meta>\n" +
            "<author>Ralf Hergert</author>\n" +
            "<date>10.12.2018</date>\n" +
            "</meta>\n" +
            "<tempo>90</tempo>\n" +
            "<music-data>" +
            "</music-data>" +
            "</song>";

        XMLSongV1 song = (XMLSongV1)JAXBContext.newInstance(XMLSongV1.class)
            .createUnmarshaller()
            .unmarshal(new StringReader(xml));

        Assert.assertNotNull("song should not be null", song);
        Assert.assertEquals("title should be", "Cantate Domino", song.getTitle());
        Assert.assertEquals("author should be", "Claudio Monteverdi", song.getAuthor());
        Assert.assertEquals("tempo should be", 90, song.getTempo());
        Assert.assertTrue("music-data should be empty", song.getMusic().getVoices().isEmpty());
        Assert.assertNotNull("song meta properties should not be null", song.getMeta());
        Assert.assertEquals("meta/author should be", "Ralf Hergert", song.getMeta().get("author"));
        Assert.assertEquals("meta/date should be", "10.12.2018", song.getMeta().get("date"));
    }

    @Test
    public void testMarshallingRandomProperties() throws JAXBException {
        final XMLSongV1 song = new XMLSongV1()
            .setTitle("Cantate Domino")
            .setAuthor("Claudio Monteverdi")
            .setTempo(90)
            .setMeta("author", "Ralf Hergert")
            .setMeta("createdDate", "10.12.2018");

        final StringWriter writer = new StringWriter();

        JAXBContext.newInstance(XMLSongV1.class)
            .createMarshaller()
            .marshal(song, writer);

        Assert.assertEquals("xml should be",
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<song>" +
            "<title>Cantate Domino</title>" +
            "<author>Claudio Monteverdi</author>" +
            "<meta>" +
            "<author>Ralf Hergert</author>" +
            "<createdDate>10.12.2018</createdDate>" +
            "</meta>" +
            "<tempo>90</tempo>" +
            "</song>",
            writer.toString());
    }

    @Test
    public void testUnmarshallingCantateDominoV1() throws JAXBException {
        XMLSongV1 song = (XMLSongV1)JAXBContext.newInstance(XMLSongV1.class)
            .createUnmarshaller()
            .unmarshal(getClass().getResourceAsStream("/cantate-domino.v1.xml"));

        Assert.assertNotNull("song should not be null", song);
        Assert.assertEquals("title should be", "Cantate Domino", song.getTitle());
        Assert.assertEquals("author should be", "Claudio Monteverdi", song.getAuthor());
        Assert.assertEquals("tempo should be", 90, song.getTempo());
        Assert.assertEquals("number of voices in music-data", 6, song.getMusic().getVoices().size());
        Assert.assertEquals("first note in first voice",
            new NoteValue(BaseNote.B, AccidentalSign.NONE, 4),
            song.getMusic().getVoices().get(0).getSequence().getElements()
                .filter(element -> element instanceof XMLNoteV1)
                .map(element -> (XMLNoteV1)element)
                .findFirst()
                .map(XMLNoteV1::getNoteValue)
                .orElse(null)

        );
    }

    @Test
    public void testUnmarshallingExampleV1() throws JAXBException {
        XMLSongV1 song = (XMLSongV1)JAXBContext.newInstance(XMLSongV1.class)
            .createUnmarshaller()
            .unmarshal(getClass().getResourceAsStream("/example.xml"));

        Assert.assertNotNull("song should not be null", song);
        Assert.assertEquals("title should be", "Brich an, o schönes Morgenlicht", song.getTitle());
        Assert.assertEquals("author should be", "J.S.Bach", song.getAuthor());
        Assert.assertEquals("tempo should be", 72, song.getTempo());
        Assert.assertEquals("number of voices should be", 4, song.getMusic().getVoices().size());
        Assert.assertNotNull("song meta properties should not be null", song.getMeta());
        Assert.assertEquals("meta/author should be", "Ralf Hergert", song.getMeta().get("author"));
        Assert.assertEquals("meta/date should be", "06.11.2008", song.getMeta().get("date"));
    }
}
