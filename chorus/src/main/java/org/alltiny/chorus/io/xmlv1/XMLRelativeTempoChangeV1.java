package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * A tempo factor is used to define temporal changes on a bar line.
 * A tempo factor can be defined like: "3/4=1/2" which means
 * "a dotted half note has the same duration as a half note".
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLRelativeTempoChangeV1 extends XMLElementV1 {

    @XmlElement
    private XMLDurationElementV1 left;
    @XmlElement
    private XMLDurationElementV1 right;

    public XMLDurationElementV1 getLeft() {
        return left;
    }

    public XMLRelativeTempoChangeV1 setLeft(XMLDurationElementV1 left) {
        this.left = left;
        return this;
    }

    public XMLDurationElementV1 getRight() {
        return right;
    }

    public XMLRelativeTempoChangeV1 setRight(XMLDurationElementV1 right) {
        this.right = right;
        return this;
    }
}
