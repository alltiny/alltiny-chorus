package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * A beam connects two or more notes with a bar, which replaces also the the flags.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLBeamV1 extends XMLDecorationV1 {

    @XmlAttribute
    private int ref;

    public int getRef() {
        return ref;
    }

    public XMLBeamV1 setRef(int ref) {
        this.ref = ref;
        return this;
    }
}
