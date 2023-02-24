/**
 * This package contains all classes resembling the Version 1 XML
 * of the chorus XML scheme.
 */
@XmlSchema(
    namespace = XMLSongV2.NAMESPACE,
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
        @XmlNs(prefix = "", namespaceURI = XMLSongV2.NAMESPACE)
    })
package org.alltiny.chorus.io.xml2v;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
