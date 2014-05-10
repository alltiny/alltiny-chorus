package org.alltiny.chorus.gui.canvas;

import org.alltiny.chorus.util.BinarySearch;

import java.util.*;

/**
 * This is a helper class to map between layout columns and midi ticks.
 */
/* package-local */ class TickColumnMap {

    private final List<TickInfo> tickColumnMap = new ArrayList<TickInfo>();

    /**
     * This method clears the complete map and deletes all mappings.
     */
    public void clear() {
        tickColumnMap.clear();
    }

    public void addMapping(int column, long tick, long tickLength) {
        tickColumnMap.add(new TickInfo().setColMin(column).setTickMin(tick).setTickMax(tick + tickLength));
        // ensure that the map is still sorted.
        Collections.sort(tickColumnMap, new Comparator<TickInfo>() {
            public int compare(TickInfo o1, TickInfo o2) {
                return o1.getColMin() - o2.getColMin();
            }
        });
        // update all max values.
        TickInfo previous = null;
        for (TickInfo current : tickColumnMap) {
            if (previous != null) {
                previous.setColMax(current.getColMin() - 1);
                previous.setTickMax(current.getTickMin() - 1);
            }
            previous = current;
        }
    }

    public TickInfo getInfoForTick(long tick) {
        if (tickColumnMap.size() == 0) {
            return null;
        }
        // search the index of the column in which the tick is. (binary search)
        int i = new BinarySearch<TickInfo,Long>(tickColumnMap, new BinarySearch.Comparator<TickInfo,Long>() {
            public int compare(TickInfo info, Long tick) {
                if (tick < info.getTickMin()) {
                    return 1; // info is greater than the tick.
                } else if (tick > info.getTickMax()) {
                    return -1; // info is small than the tick.
                } else {
                    return 0; // the tick is in info.
                }
            }
        }).getIndexOf(tick);

        return tickColumnMap.get(i);
    }

    public TickInfo getInfoForColumn(int col) {
        int i = new BinarySearch<TickInfo,Integer>(tickColumnMap, new BinarySearch.Comparator<TickInfo,Integer>() {
            public int compare(TickInfo info, Integer col) {
                if (col < info.getColMin()) {
                    return 1; // info is greater than the tick.
                } else if (col > info.getColMax()) {
                    return -1; // info is small than the tick.
                } else {
                    return 0; // the tick is in info.
                }
            }
        }).getIndexOf(col);

        return tickColumnMap.get(i);
    }
}
