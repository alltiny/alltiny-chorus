package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single voice which contains a main sequence
 * and zero or many inline-sequences.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLVoiceV1 {

    @XmlAttribute
    private String head;
    @XmlAttribute
    private String name;
    @XmlElement
    private XMLSequenceV1 sequence;
    @XmlElement(name = "inlinesequence")
    private List<XMLInlineSequenceV1> inlineSequences = new ArrayList<XMLInlineSequenceV1>();

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSequence(XMLSequenceV1 sequence) {
        this.sequence = sequence;
    }

    public XMLSequenceV1 getSequence() {
        return sequence;
    }

    public void addInlineSequence(XMLInlineSequenceV1 sequence) {
        inlineSequences.add(sequence);
    }

    public List<XMLInlineSequenceV1> getInlineSequences() {
        return inlineSequences;
    }
}
