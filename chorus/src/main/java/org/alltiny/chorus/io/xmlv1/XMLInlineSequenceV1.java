package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A sequence which can be hooked into a {@link XMLSequenceV1}. Unlike
 * {@link XMLSequenceV1} an inline-sequence does only allow certain
 * {@link XMLElementV1}s.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLInlineSequenceV1 {

    @XmlAttribute(name = "anchorref")
    private Integer anchorRef;

    @XmlElements({
        @XmlElement(name = "note", type = XMLNoteV1.class),
        @XmlElement(name = "rest", type = XMLRestV1.class),
        @XmlElement(name = "dynamic", type = XMLDynamicElementV1.class)
    })
    private final List<XMLElementV1> elements = new ArrayList<>();

    public Integer getAnchorRef() {
        return anchorRef;
    }

    public XMLInlineSequenceV1 setAnchorRef(Integer anchorRef) {
        this.anchorRef = anchorRef;
        return this;
    }

    public Stream<XMLElementV1> getElements() {
        return elements.stream();
    }

    public XMLInlineSequenceV1 addElement(XMLElementV1 element) {
        elements.add(element);
        return this;
    }
}
