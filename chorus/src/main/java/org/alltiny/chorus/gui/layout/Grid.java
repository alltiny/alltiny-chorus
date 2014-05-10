package org.alltiny.chorus.gui.layout;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a three dimensional grid.
 * @param <Type> type of object this grid can organize.
 */
public class Grid<Type> {

    private List<List<List<Type>>> grid = new ArrayList<List<List<Type>>>();

    public void add(int x, int y, Type object) {
        // ensure that index for x exists.
        while (x >= grid.size()) {
            grid.add(new ArrayList<List<Type>>());
        }
        List<List<Type>> xList = grid.get(x);

        // ensure that index for y exists.
        while (y >= xList.size()) {
            xList.add(new ArrayList<Type>());
        }
        List<Type> yList = xList.get(y);

        // add the given object.
        yList.add(object);
    }
}
