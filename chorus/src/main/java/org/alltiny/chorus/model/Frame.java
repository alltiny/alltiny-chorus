package org.alltiny.chorus.model;

import org.alltiny.chorus.dom.Bar;
import org.alltiny.chorus.dom.Element;

import java.util.ArrayList;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 26.02.2009 19:11:21
 */
public class Frame {

    private ArrayList<Cell> cells = new ArrayList<Cell>();
    private Bar bar = null;

    /** This is the offset in ticks, where this frame starts. */
    private long tickOffset = 0;
    private long tickLength = 0;
    private boolean newBarStarts = false;

    public Frame() {
        this(0,0);
    }

    public Frame(int numVoice, long tickOffset) {
        // initialize the array with the given number of cells.
        for (int i = 0; i < numVoice; i++) {
            cells.add(new Cell());
        }
        this.tickOffset = tickOffset;
    }

    public long getTickOffset() {
        return tickOffset;
    }

    public long getTickLength() {
        return tickLength;
    }

    public void setTickOffset(long tickOffset) {
        this.tickOffset = tickOffset;
    }

    public boolean isNewBarStarts() {
        return newBarStarts;
    }

    public void setNewBarStarts(boolean newBarStarts) {
        this.newBarStarts = newBarStarts;
    }

    public void add(int voice, Element element, long elementLength) {
        cells.get(voice).add(element);
        if (elementLength != 0) {
            if (tickLength == 0 || tickLength > elementLength) {
                tickLength = elementLength; // the smallest counts, but not 0.
            }
        }
    }

    public Cell get(int voice) {
        return cells.get(voice);
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }
}
