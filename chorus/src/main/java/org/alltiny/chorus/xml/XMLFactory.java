package org.alltiny.chorus.xml;

import org.xml.sax.SAXException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 19.11.2008 20:48:09
 */
public interface XMLFactory<Type> {

    public Type createInstance() throws SAXException;
}
