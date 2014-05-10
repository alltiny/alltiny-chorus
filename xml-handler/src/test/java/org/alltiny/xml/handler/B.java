package org.alltiny.xml.handler;

import java.util.List;
import java.util.ArrayList;

/**
 * Used as test object.
 */
public class B {

    private final List<C> children = new ArrayList<C>();

    public List<C> getChildren() {
        return children;
    }

    public void addChild(C child) {
        children.add(child);
    }
}
