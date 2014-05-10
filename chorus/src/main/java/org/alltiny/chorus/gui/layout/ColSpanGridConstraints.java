package org.alltiny.chorus.gui.layout;

/**
 * This class is the constraints for {@link MedianGridLayout}.
 */
public class ColSpanGridConstraints {

    private final int row;
    private final int startCol;
    private final int endCol;

    public ColSpanGridConstraints(int row, int startCol, int endCol) {
        this.row = row;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    public int getRow() {
        return row;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndCol() {
        return endCol;
    }
}