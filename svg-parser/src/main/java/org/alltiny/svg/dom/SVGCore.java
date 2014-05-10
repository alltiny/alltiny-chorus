package org.alltiny.svg.dom;

import java.net.URI;

/**
 * This is the super class of all svg elements.
 * It defines the attributes that every svg element supports.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.04.2009
 */
public abstract class SVGCore {

    private String id; // a unique id
    private URI xmlbase; // the xml base to which this element belongs.
    private String xmllang; // the language to which this element is assigned.
    private XMLSpacing xmlspace = XMLSpacing.DEFAULT; // xml spacing of the element.

    protected SVGCore(String id) {
        this.id = id;
    }
}
