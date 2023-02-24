package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * A bound binds two or more notes to legato.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLBoundV1 extends XMLDecorationV1 {

    @XmlAttribute
    private int ref;

    public int getRef() {
        return ref;
    }

    public XMLBoundV1 setRef(int ref) {
        this.ref = ref;
        return this;
    }
}
