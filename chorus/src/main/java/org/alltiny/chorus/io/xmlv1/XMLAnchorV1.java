package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * An anchor is an invisible element which is to mark where
 * in the main sequence another inline-sequence is hooked in.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLAnchorV1 extends XMLElementV1 {

    @XmlAttribute
    private int ref;

    public int getRef() {
        return ref;
    }

    public XMLAnchorV1 setRef(int ref) {
        this.ref = ref;
        return this;
    }
}
