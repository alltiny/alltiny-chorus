package org.alltiny.chorus.io.xmlv1;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.alltiny.chorus.base.type.AccidentalSign;

/**
 * Allow to show accidental sign even if then a technically not necessary.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLAccidentalV1 extends XMLDecorationV1 {

    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(AccidentalSignAdapter.class)
    private AccidentalSign sign = AccidentalSign.NONE;

    public XMLAccidentalV1() {}

    public XMLAccidentalV1(AccidentalSign sign) {
        this.sign = sign;
    }

    public AccidentalSign getSign() {
        return sign;
    }

    public void setSign(AccidentalSign sign) {
        this.sign = sign;
    }
}
