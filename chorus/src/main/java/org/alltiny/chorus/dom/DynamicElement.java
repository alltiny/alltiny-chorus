package org.alltiny.chorus.dom;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 23.09.2009 17:52:35
 */
public class DynamicElement extends Element {

    private String key;

    public DynamicElement(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
