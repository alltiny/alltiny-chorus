package org.alltiny.chorus.xml;

import org.alltiny.chorus.dom.Note;
import org.alltiny.chorus.dom.NoteMidiValue;
import org.alltiny.chorus.dom.decoration.Decoration;
import org.alltiny.chorus.dom.decoration.Fermata;
import org.alltiny.chorus.dom.decoration.Accidental;
import org.alltiny.xml.handler.XMLHandler;
import org.alltiny.xml.handler.AssignHandler;
import org.alltiny.xml.handler.AssignException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.text.ParseException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 08.11.2008 11:58:22
 */
public class NoteXMLHandler extends XMLHandler<Note> {

    private final Note note;

    public NoteXMLHandler(final Attributes attributes, AssignHandler<Note> assignHandler) throws SAXException {
        super(assignHandler);
        int duration, division;
        try {
            duration = Integer.parseInt(attributes.getValue("duration"));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'duration\' is undefined or wrong. only numbers are allowed.", e);
        }
        try {
            division = Integer.parseInt(attributes.getValue("division"));
        } catch (NumberFormatException e) {
            throw new SAXException("Attribute \'division\' is undefined or wrong. only numbers are allowed.", e);
        }
        String value = attributes.getValue("value");
        if (value == null)
            throw new SAXException("Attribute \'value\' is undefined.");

        try {
            note = NoteMidiValue.createNote(value, duration, division);
        } catch (ParseException e) {
            throw new SAXException("Attribute \'value\' is defined wrong.", e);
        }
    }

    protected XMLHandler getHandlerInstance(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("beam".equals(qName)) {
            return new SimpleElementXMLHandler<Decoration>(new BeamElementFactory(attributes), new DecorationAssignHandler(note));
        }
        if ("bound".equals(qName)) {
            return new SimpleElementXMLHandler<Decoration>(new BoundElementFactory(attributes), new DecorationAssignHandler(note));
        }
        if ("triplet".equals(qName)) {
            return new SimpleElementXMLHandler<Decoration>(new TripletElementFactory(attributes), new DecorationAssignHandler(note));
        }
        if ("fermata".equals(qName)) {
            return new SimpleElementXMLHandler<Decoration>(new Fermata(), new DecorationAssignHandler(note));
        }
        if ("accidental".equals(qName)) {
            return new SimpleElementXMLHandler<Accidental>(new AccidentalElementFactory(attributes), new AssignHandler<Accidental>() {
                public void assignNode(Accidental node) {
                    note.setAccidental(node.getSign());
                }
            });
        }
        if ("lyric".equals(qName)) {
            return new StringXMLHandler(new AssignHandler<String>() {
                public void assignNode(String node) throws AssignException {
                    note.setLyric(node);
                }
            });
        }
        throw new SAXException("Element '" + qName + "' can not be resolved from " + getClass());
    }

    public Note getObject() {
        return note;
    }

    private static class DecorationAssignHandler implements AssignHandler<Decoration> {

        private final Note note;

        public DecorationAssignHandler(Note note) {
            this.note = note;
        }

        public void assignNode(Decoration node) throws AssignException {
            note.addDecoration(node);
        }
    }
}
