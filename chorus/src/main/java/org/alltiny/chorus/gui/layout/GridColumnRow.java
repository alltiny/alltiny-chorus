package org.alltiny.chorus.gui.layout;

import org.alltiny.chorus.render.Visual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * This class manages a row or a column in the grid.
 */
/* package-local */ abstract class GridColumnRow {

    protected final HashMap<Integer, List<Visual>> visuals = new HashMap<Integer,List<Visual>>();

    private double medianOffset;
    private double leadingExtend;
    private double trailingExtend;

    /**
     * This method adds the given visual at the wanted index.
     * If there is already a visual at this index, the old onw will be dropped.
     */
    public void addVisual(Visual visual, int index) {
        List<Visual> list = visuals.get(index);
        if (list == null) { // create the list if it does not exist.
            list = new ArrayList<Visual>();
        }
        list.add(visual);
        visuals.put(index, list);
    }

    /**
     * @return the visual at the given index. null if none exists.
     */
    public List<Visual> getVisuals(int index) {
        return visuals.get(index);
    }

    public void removeVisual(Visual visual, int index) {
        List<Visual> list = visuals.get(index);
        if (list != null) {
            list.remove(visual);
            if (list.isEmpty()) {
                visuals.remove(index);
            }
        }
    }

    public boolean isEmpty() {
        return visuals.isEmpty();
    }

    public void setMedianOffset(double value) {
        double old = medianOffset;
        medianOffset = value;
        if (old != value) {
            updateAll(); // update all if changed
        }
    }

    public void setLeadingExtend(double value) {
        double old = leadingExtend;
        leadingExtend = value;
        if (old != value) {
            updateAll(); // update all if changed
        }
    }

    public void setTrailingExtend(double value) {
        double old = trailingExtend;
        trailingExtend = value;
        if (old != value) {
            updateAll(); // update all if changed
        }
    }

    public double getMedianOffset() {
        return medianOffset;
    }

    public double getLeadingExtend() {
        return leadingExtend;
    }

    public double getTrailingExtend() {
        return trailingExtend;
    }

    public Collection<Visual> getAllVisuals() {
        List<Visual> all = new ArrayList<Visual>();
        for (List<Visual> list : visuals.values()) {
            all.addAll(list);
        }
        return all;

    }

    /**
     * This method is triggered when the offset or the extend has change.
     * The implementation of this method must update all visuals correspondingly.
     */
    protected abstract void updateAll();
}
