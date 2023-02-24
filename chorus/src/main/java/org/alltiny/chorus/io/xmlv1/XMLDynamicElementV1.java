package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * Allows to define changes in the dynamic.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLDynamicElementV1 extends XMLElementV1 {

    @XmlAttribute(name = "value")
    private String value;

    public String getValue() {
        return value;
    }

    public XMLDynamicElementV1 setValue(String value) {
        this.value = value;
        return this;
    }
}
