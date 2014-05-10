package org.alltiny.chorus.gui.canvas;

/**
 * This is a helper class for {@link MusicCanvas} to store and handle with the
 * information about the tick offset and tick length of the frames.
 */
/* package-local*/ class TickInfo {

    private int colMin;
    private int colMax;
    private long tickMin = 0;
    private long tickMax = 0;

    public int getColMin() {
        return colMin;
    }

    public TickInfo setColMin(int colMin) {
        this.colMin = colMin;
        return this;
    }

    public int getColMax() {
        return colMax;
    }

    public TickInfo setColMax(int colMax) {
        this.colMax = colMax;
        return this;
    }

    public long getTickMin() {
        return tickMin;
    }

    public TickInfo setTickMin(long tickMin) {
        this.tickMin = tickMin;
        return this;
    }

    public long getTickMax() {
        return tickMax;
    }

    public TickInfo setTickMax(long tickMax) {
        this.tickMax = tickMax;
        return this;
    }
}
