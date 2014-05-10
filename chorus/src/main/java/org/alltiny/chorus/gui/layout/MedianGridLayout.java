package org.alltiny.chorus.gui.layout;

import org.alltiny.chorus.render.Visual;
import org.alltiny.chorus.util.BinarySearch;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This layout manager implementation performs the layout on visual components
 * with a median.
 */
public class MedianGridLayout implements LayoutManager2 {

    private final ArrayList<GridRow> rows = new ArrayList<GridRow>();
    private final ArrayList<GridColumn> cols = new ArrayList<GridColumn>();
    private final HashMap<Visual,Object> visuals = new HashMap<Visual, Object>();
    private final HashMap<ColSpanGridConstraints,Visual> colSpanningConstraints = new HashMap<ColSpanGridConstraints,Visual>();

    private double maxWidth = 0;
    private double maxHeight = 0;

    public void addLayoutComponent(Component comp, Object constraints) {
        // ignore the given component in layout if it is no Visual or no Constraints was given.
        if (comp instanceof Visual) {
            Visual visual = (Visual)comp;

            if (constraints instanceof GridConstraints) {
                GridConstraints gc = (GridConstraints)constraints;
                this.visuals.put(visual, gc);
                // add this visual to the grid.
                addVisualToRowGrid(visual, gc.getCol(), gc.getRow());
                addVisualToColGrid(visual, gc.getCol(), gc.getRow());
            } else if (constraints instanceof ColSpanGridConstraints) {
                ColSpanGridConstraints csgc = (ColSpanGridConstraints)constraints;
                this.visuals.put(visual, csgc);
                this.colSpanningConstraints.put(csgc, visual);
                // add this visual to the grid.
                for (int col = csgc.getStartCol(); col <= csgc.getEndCol(); col++) {
                    addVisualToRowGrid(visual, col, csgc.getRow());
                    addVisualToColGrid(visual, col, csgc.getRow());
                }
            }
        }
    }

    private void addVisualToRowGrid(Visual visual, int colIndex, int rowIndex) {
        // assign the given visual to its row. brackets are for scoping.
        while (rowIndex >= rows.size()) { // ensure that a GridRow for the given row index exists.
            rows.add(new GridRow());
        }
        // add the visual to this row.
        rows.get(rowIndex).addVisual(visual, colIndex);
    }

    private void addVisualToColGrid(Visual visual, int colIndex, int rowIndex) {
        // assign the given visual to its row. brackets are for scoping.
        while (colIndex >= cols.size()) { // ensure that a GridRow for the given row index exists.
            cols.add(new GridColumn());
        }
        // add the visual to this column.
        cols.get(colIndex).addVisual(visual, rowIndex);
    }

    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    public void invalidateLayout(Container target) {
        maxWidth = 0;
        maxHeight = 0;

        double prevColOffset = 0;
        // and recalculate the new one.
        for (int col = 0; col < cols.size(); col++) {
            final GridColumn currCol = this.cols.get(col);
            double columnOffset = 0;
            double maxExtLeft = 0;
            double maxExtRight = 0;

            for (int row = 0; row < rows.size(); row++) {
                /*
                 * To layout the grid we iterate through the first row from left to right.
                 * For every cell the distance to the UPPER and the LEFT cell is checked.
                 * If a collision is detected, then the row- or the column-offset of the
                 * current row/column will be increased. That ensures, that the grid is
                 * layouted from left to right and from top to bottom.
                 */
                // skip if no visual exists at this position.
                if (currCol.getVisuals(row) == null) {
                    continue;
                }

                for (Visual cell : currCol.getVisuals(row)) {
                    Rectangle2D box = cell.getBounds2D();
                    Rectangle2D pad = cell.getPadding();

                    maxExtLeft  = Math.max(maxExtLeft,  -box.getMinX());
                    maxExtRight = Math.max(maxExtRight,  box.getMaxX());

                    /*
                     * Check against the preceding visual left.
                     */
                    java.util.List<Visual> lefts = null;
                    // search for the next left cell with visuals in it.
                    for (int i = col - 1; lefts == null && i >= 0; i--) {
                        lefts = this.cols.get(i).getVisuals(row);
                    }
                    // iterate through all visuals in the left cell.
                    if (lefts == null) { // if no visual left beside this one was found.
                        columnOffset = Math.max(columnOffset, -box.getMinX() - pad.getMinX());
                    } else { // check against the ceft left.
                        for (Visual left : lefts) {
                            // determine the padding between this and more left cell.
                            double padding = Math.max(-pad.getMinX(), left.getPadding().getMaxX());
                            // now check that the offset is sufficient for the boxes and the padding.
                            columnOffset = Math.max(columnOffset, -box.getMinX() + padding + left.getBounds2D().getMaxX() + prevColOffset);
                        }
                    }
                }
            }
            currCol.setMedianOffset(columnOffset);
            currCol.setLeadingExtend(maxExtLeft);
            currCol.setTrailingExtend(maxExtRight);
            prevColOffset = columnOffset;
            maxWidth = Math.max(maxWidth, columnOffset + maxExtRight);
        }

        // and recalculate the new one.
        for (int row = 0; row < rows.size(); row++) {
            final GridRow currRow = this.rows.get(row);
            double maxExtTop = 0;
            double maxExtBottom = 0;

            // the row offset of the current row is at least the offset of the previous row.
            if (row > 0) {
                currRow.setMedianOffset(rows.get(row - 1).getMedianOffset());
            }

            for (int col = 0; col < cols.size(); col++) {
                /*
                 * To layout the grid we iterate through the first row from left to right.
                 * For every cell the distance to the UPPER and the LEFT cell is checked.
                 * If a collision is detected, then the row- or the column-offset of the
                 * current row/column will be increased. That ensures, that the grid is
                 * layouted from left to right and from top to bottom.
                 */
                // skip if no visual exists at this position.
                if (currRow.getVisuals(col) == null) {
                    continue;
                }
                for (Visual cell : currRow.getVisuals(col)) {

                    Rectangle2D box = cell.getBounds2D();
                    Rectangle2D pad = cell.getPadding();

                    maxExtTop    = Math.max(maxExtTop,    -box.getMinY());
                    maxExtBottom = Math.max(maxExtBottom,  box.getMaxY());

                    /*
                     * Check against the preceding visual above.
                     */
                    java.util.List<Visual> tops = null;
                    double medianOfAbove = 0;
                    for (int i = row - 1; tops == null && i >= 0; i--) {
                        tops = rows.get(i).getVisuals(col);
                        medianOfAbove = rows.get(i).getMedianOffset();
                    }

                    if (tops == null) {
                        /* if no visual on top this one was found, then  check if
                         * there is enough space for the current visual. if not
                         * then increase the offset this row and all rows above. */
                        final double diff = -box.getMinY() - pad.getMinY() - currRow.getMedianOffset();
                        if (diff > 0) {
                            for (int i = row; i >= 0; i--) { // correct the row offset of the current and all rows above.
                                rows.get(i).setMedianOffset(diff + rows.get(i).getMedianOffset());
                            }
                        }
                    } else { // check against the left top.
                        for (Visual top : tops) {
                            // determine the padding between this and more top cell.
                            double padding = Math.max(-pad.getMinY(), top.getPadding().getMaxY());
                            // now check that the offset is sufficient for the boxes and the padding.
                            currRow.setMedianOffset(Math.max(currRow.getMedianOffset(), -box.getMinY() + padding + top.getBounds2D().getMaxY() + medianOfAbove));
                        }
                    }
                }
            }
            currRow.setLeadingExtend(maxExtTop);
            currRow.setTrailingExtend(maxExtBottom);
            maxHeight = Math.max(maxHeight, currRow.getMedianOffset() + maxExtBottom);
        }

        updateColSpanningComponents();
    }

    private void updateColSpanningComponents() {
        for (ColSpanGridConstraints constraints : colSpanningConstraints.keySet()) {
            Visual visual = colSpanningConstraints.get(constraints);
            int minX = (int)Math.round(cols.get(constraints.getStartCol()).getMedianOffset());
            int maxX = (int)Math.round(cols.get(constraints.getEndCol()).getMedianOffset());
            int y    = (int)Math.round(rows.get(constraints.getRow()).getMedianOffset());
            visual.setLocation(minX, y);
            visual.setAbsMedianX(minX);
            visual.setAbsMedianY(y);
            visual.setSize(maxX - minX, visual.getHeight());
        }
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
        Object constraints = this.visuals.remove(comp);
        if (constraints instanceof GridConstraints) {
            removeVisualFromGrid((Visual)comp, ((GridConstraints)constraints).getRow(), ((GridConstraints)constraints).getCol());
        } else if (constraints instanceof ColSpanGridConstraints) {
            ColSpanGridConstraints csgc = (ColSpanGridConstraints)constraints;
            colSpanningConstraints.remove(csgc);
            for (int col = csgc.getStartCol(); col <= csgc.getEndCol(); col++) {
                removeVisualFromGrid((Visual)comp, csgc.getRow(), col);
            }
        }

        // remove empty rows. always begin with the last to avoid destroying the order of indexes.
        while (rows.size() > 0 && rows.get(rows.size() - 1).isEmpty()) {
            rows.remove(rows.size() - 1);
        }

        // remove empty cols. always begin with the last to avoid destroying the order of indexes.
        while (cols.size() > 0 && cols.get(cols.size() - 1).isEmpty()) {
            cols.remove(cols.size() - 1);
        }
    }

    private void removeVisualFromGrid(Visual visual, int rowIndex, int colIndex) {
        GridRow row = rows.get(rowIndex);
        if (row != null) {
            row.removeVisual(visual, colIndex);
        }

        GridColumn col = cols.get(colIndex);
        if (col != null) {
            col.removeVisual(visual, rowIndex);
        }
    }

    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension((int)Math.ceil(maxWidth), (int)Math.ceil(maxHeight));
    }

    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    public void layoutContainer(Container parent) {
    }

    public Collection<Visual> getComponentsOfColumn(int colIndex) {
        return cols.get(colIndex).getAllVisuals();
    }

    /**
     * This method will determine all components which are in the given bounds.
     */
    public Collection<Component> getComponentsInClipBounds(Rectangle bounds) {
        Collection<Component> components = new HashSet<Component>();

        int minCol = getMinColumnIndexAtPosition(bounds.getMinX());
        int maxCol = getMaxColumnIndexAtPosition(bounds.getMaxX());

        // add all components of the determined columns.
        for (int i = minCol; i <= maxCol; i++) {
            components.addAll(cols.get(i).getAllVisuals());
        }

        return components;
    }

    public int getMinColumnIndexAtPosition(double position) {
        return new BinarySearch<GridColumn,Double>(cols, new BinarySearch.Comparator<GridColumn,Double>() {
            public int compare(GridColumn gridColumn, Double qual) {
                if (qual < gridColumn.getMedianOffset()) {
                    return 1; // grid column is still too big.
                } else if (qual > gridColumn.getMedianOffset() + gridColumn.getTrailingExtend()) {
                    return -1; // grid column is too small.
                } else {
                    return 0;
                }
            }
        }).getIndexOf(position);
    }

    public int getMaxColumnIndexAtPosition(double position) {
        return new BinarySearch<GridColumn,Double>(cols, new BinarySearch.Comparator<GridColumn,Double>() {
            public int compare(GridColumn gridColumn, Double qual) {
                if (qual > gridColumn.getMedianOffset()) {
                    return -1; // grid column is still too big.
                } else if (qual < gridColumn.getMedianOffset() - gridColumn.getLeadingExtend()) {
                    return 1; // grid column is too small.
                } else {
                    return 0;
                }
            }
        }).getIndexOf(position);
    }

    public double getAbsMedianX(int column) {
        return cols.get(column).getMedianOffset();
    }

    public double getExtendRight(int column) {
        return cols.get(column).getTrailingExtend();
    }
}
