package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.alltiny.chorus.base.type.NoteValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class represents a note.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLNoteV1 extends XMLDurationElementV1 {

    @XmlAttribute(name = "value")
    @XmlJavaTypeAdapter(NoteValueAdapter.class)
    private NoteValue noteValue;

    @XmlElements({
        @XmlElement(name = "beam", type = XMLBeamV1.class),
        @XmlElement(name = "bound", type = XMLBoundV1.class),
        @XmlElement(name = "triplet", type = XMLTripletV1.class),
        @XmlElement(name = "fermata", type = XMLFermataV1.class)
    })
    private final List<XMLDecorationV1> decoration = new ArrayList<>();

    @XmlElement
    private XMLAccidentalV1 accidental;

    @XmlElement
    private String lyric;

    public NoteValue getNoteValue() {
        return noteValue;
    }

    public XMLNoteV1 setNoteValue(NoteValue noteValue) {
        this.noteValue = noteValue;
        return this;
    }

    public Stream<XMLDecorationV1> getDecorations() {
        return decoration.stream();
    }

    public XMLNoteV1 addDecoration(XMLDecorationV1 decoration) {
        this.decoration.add(decoration);
        return this;
    }

    public XMLAccidentalV1 getAccidental() {
        return accidental;
    }

    public XMLNoteV1 setAccidental(XMLAccidentalV1 accidental) {
        this.accidental = accidental;
        return this;
    }

    public String getLyric() {
        return lyric;
    }

    public XMLNoteV1 setLyric(String lyric) {
        this.lyric = lyric;
        return this;
    }
}
