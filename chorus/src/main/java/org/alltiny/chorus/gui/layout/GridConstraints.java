package org.alltiny.chorus.gui.layout;

/**
 * This class is the constraints for {@link MedianGridLayout}.
 */
public class GridConstraints {

    private final int row;
    private final int col;

    public GridConstraints(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
