package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import org.alltiny.chorus.base.type.Clef;
import org.alltiny.chorus.base.type.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A sequence of {@link XMLElementV1}s.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLSequenceV1 {

    @XmlAttribute
    private Clef clef;

    @XmlAttribute
    private Key key;

    @XmlElements({
        @XmlElement(name = "bar", type = XMLBarV1.class),
        @XmlElement(name = "note", type = XMLNoteV1.class),
        @XmlElement(name = "rest", type = XMLRestV1.class),
        @XmlElement(name = "dynamic", type = XMLDynamicElementV1.class),
        @XmlElement(name = "anchor", type = XMLAnchorV1.class),
        @XmlElement(name = "repeat-begin", type = XMLRepeatBeginV1.class),
        @XmlElement(name = "repeat-end", type = XMLRepeatEndV1.class),
        @XmlElement(name = "relTempoChange", type = XMLRelativeTempoChangeV1.class),
        @XmlElement(name = "absTempoChange", type = XMLAbsoluteTempoChangeV1.class)
    })
    private final List<XMLElementV1> elements = new ArrayList<>();

    public Clef getClef() {
        return clef;
    }

    public XMLSequenceV1 setClef(Clef clef) {
        this.clef = clef;
        return this;
    }

    public Key getKey() {
        return key;
    }

    public XMLSequenceV1 setKey(Key key) {
        this.key = key;
        return this;
    }

    public Stream<XMLElementV1> getElements() {
        return elements.stream();
    }

    public XMLSequenceV1 addElement(XMLElementV1 element) {
        elements.add(element);
        return this;
    }
}
