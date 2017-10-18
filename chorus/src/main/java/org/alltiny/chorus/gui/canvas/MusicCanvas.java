package org.alltiny.chorus.gui.canvas;

import org.alltiny.chorus.dom.clef.Clef;
import org.alltiny.chorus.dom.Element;
import org.alltiny.chorus.dom.Note;
import org.alltiny.chorus.dom.Rest;
import org.alltiny.chorus.dom.decoration.AccidentalSign;
import org.alltiny.chorus.dom.decoration.Bound;
import org.alltiny.chorus.dom.decoration.Decoration;
import org.alltiny.chorus.dom.decoration.Triplet;
import org.alltiny.chorus.gui.layout.ColSpanGridConstraints;
import org.alltiny.chorus.gui.layout.GridConstraints;
import org.alltiny.chorus.gui.layout.MedianGridLayout;
import org.alltiny.chorus.render.Visual;
import org.alltiny.chorus.render.element.*;
import org.alltiny.chorus.render.element.Cell;
import org.alltiny.chorus.model.SongModel;
import org.alltiny.chorus.model.MusicDataModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class MusicCanvas extends JComponent implements Scrollable {

    public static final String CURRENT_CURSOR_POSITION = "currentCursorPosition";
    public static final String ZOOM_FACTOR = "zoomFactor";

    private static final int HPADDING_LINE = 40;
    private static final int VPADDING_LINE = 20;
    private static final double LEAD_OFFSET = 6.5;

    private final SongModel model;
    private final MusicDataModel songModel;
    private final MedianGridLayout layout;

    // this is a helper list to get the correct column for the current tick.
    private final TickColumnMap tickColumnMap = new TickColumnMap();

    private Zoom zoom = Zoom.FIT_TO_HEIGHT;
    private double zoomFactor = 1;

    private Rectangle cursorPosition;

    public MusicCanvas(SongModel model, final MusicDataModel songModel) {
        this.model = model;
        this.songModel = songModel;
        layout = new MedianGridLayout();
        setLayout(layout);
        setOpaque(true);
        setDoubleBuffered(true);

        model.addPropertyChangeListener(SongModel.CURRENT_SONG, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                update();
                revalidate();
                repaint();
            }
        });
        update();
    }

    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setBackground(Color.WHITE);
        g.setColor(Color.BLACK);
        g.clearRect(0,0,getWidth(),getHeight());
        g.scale(zoomFactor,zoomFactor);
        g.transform(AffineTransform.getTranslateInstance(VPADDING_LINE, HPADDING_LINE));

        // only draw all components in clipping bounds.
        for (Component comp : layout.getComponentsInClipBounds(g.getClipBounds())) {
            comp.paint(g);
        }
    }

    private void update() {
        final int numVoice = songModel.getNumberOfVoices();
        // remove all components
        removeAll();
        // clear the tick column helper map.
        tickColumnMap.clear();

        // ArrayList
        ArrayList<Lines> lines = new ArrayList<Lines>();
        for (int i = 0; i < numVoice; i++) {
            lines.add(new Lines());
            // assign the lines also to this container to be drawn. note that the lines must not be layed out.
            add(lines.get(i));
        }

        int currentColumn = 0;
        final int linesStartCol = currentColumn;
        // draw a leading BarLine...
        for (int i = 0; i < numVoice; i++) {
            // create the starting BarLine on the left.
            Visual barLine = new BarLine().setPadding(new Rectangle2D.Float(0,0,0,0));
            // finally add the cell to the grid.
            add(barLine, new GridConstraints(getNotesRowOfVoice(i), currentColumn));
        }
        currentColumn++;

        // draw clefts
        for (int i = 0; i < numVoice; i++) {
            Cell cell = new Cell();
            if (model.getSong().getMusic().getVoices().get(i).getSequence().getClef() == Clef.G) {
                cell.addVisualToNotes(new ClefG());
            }
            if (model.getSong().getMusic().getVoices().get(i).getSequence().getClef() == Clef.G8basso) {
                cell.addVisualToNotes(new ClefG8basso());
            }
            if (model.getSong().getMusic().getVoices().get(i).getSequence().getClef() == Clef.F) {
                cell.addVisualToNotes(new ClefF());
            }
            // finally add the cell to the grid.
            add(cell, new GridConstraints(getNotesRowOfVoice(i), currentColumn));
        }
        currentColumn++;

        // draw keys
        for (int i = 0; i < numVoice; i++) {
            add(new KeyRender(model.getSong().getMusic().getVoices().get(i).getSequence()), new GridConstraints(getNotesRowOfVoice(i), currentColumn));
        }
        currentColumn++;

        /*
         * The helper maps a created here because the notes are processed by frames and not be voices.
         */
        // create a key map for each voice.
        KeyMapHelper[] keyMapper = new KeyMapHelper[numVoice];
        // create a helper map for bindings.
        HashMap<Integer, BindingHelper>[] bindingHelpers = new HashMap[numVoice];
        TripletHelper[] tripletHelpers = new TripletHelper[numVoice];
        for (int i = 0; i < numVoice; i++) {
            keyMapper[i] = new KeyMapHelper(model.getSong().getMusic().getVoices().get(i).getSequence().getKey());
            bindingHelpers[i] = new HashMap<Integer,BindingHelper>();
        }

        int numberOfBeat = 0;

        for (org.alltiny.chorus.model.Frame frame : songModel.getFrames()) {
            // if this frame should start with a bar, then add a bar to the lines.
            if (frame.isNewBarStarts()) {
                numberOfBeat++;
                // add a number to the beat.
                add(new BeatNumber(numberOfBeat), new GridConstraints(0, currentColumn));

                if (numberOfBeat != 1) { // don't render a bar-line if this is the very first beat.
                    for (int i = 0; i < numVoice; i++) {
                        // add a bar line to each voice line.
                        add(new BarLine(), new GridConstraints(getNotesRowOfVoice(i), currentColumn));
                        // reset all key mappings.
                        keyMapper[i].clear();
                    }
                    currentColumn++;
                }
                // render beat-length
                if (frame.getBar() != null) {
                    for (int i = 0; i < numVoice; i++) {
                        add(new BeatRenderer(frame.getBar()), new GridConstraints(getNotesRowOfVoice(i), currentColumn));
                    }
                    currentColumn++;
                }
            }

            // create a TickHelper for this frame only if this frame has elements with duration.
            if (frame.getTickLength() > 0) {
                tickColumnMap.addMapping(currentColumn, frame.getTickOffset(), frame.getTickLength());
            }

            for (int voiceIndex = 0; voiceIndex < numVoice; voiceIndex++) {
                Cell cell = new Cell();
                // create a helper map for bindings.
                HashMap<Integer, BindingHelper> bindingHelper = bindingHelpers[voiceIndex];

                for (Element element : frame.get(voiceIndex)) {
                    if (element instanceof Note) {
                        Note note = (Note)element;
                        // get the current accidental modification for this note.
                        AccidentalSign sign = keyMapper[voiceIndex].getAccidentalSign(note.getOctave(), note.getNote());
                        NoteRender renderer = new NoteRender(note);
                        // check whether the current modification conflicts with the note accidental.
                        if (sign != note.getSign()) {
                            // register the modification to the mapping to influence trailing notes.
                            keyMapper[voiceIndex].setAccidentalSign(note.getOctave(), note.getNote(), note.getSign());
                            renderer.setDrawAccidentalSign(true);
                        }
                        // check if lyrics are assign to the note.
                        if (note.getLyric() != null) {
                            add(new Lyrics(note.getLyric()), new GridConstraints(getLyricsRowOfVoice(voiceIndex), currentColumn));
                        }
                        // check for bindings
                        Bound bound = getBoundDecorFromNote(note);
                        if (bound != null) {
                            // check if a previous binding already exists
                            if (bindingHelper.containsKey(bound.getRef())) {
                                BindingHelper helper = bindingHelper.get(bound.getRef());
                                // create a new binding.
                                Binding binding = new Binding().setStartElement(helper.getRender()).setEndElement(renderer);
                                // add the binding to the layout.
                                add(binding, new ColSpanGridConstraints(getNotesRowOfVoice(voiceIndex), helper.getColumn(), currentColumn));
                            }
                            // add the current note render to be able to create a binding for it.
                            bindingHelper.put(bound.getRef(), new BindingHelper(currentColumn, renderer));
                        } else {
                            // remove all binding infos if the note is unbound.
                            bindingHelper.clear();
                        }
                        // check for triplets
                        Triplet triplet = getTripletDecorFromNote(note);
                        if (triplet != null) {
                            // does a current triplet already exist?
                            if (tripletHelpers[voiceIndex] != null) {
                                if (tripletHelpers[voiceIndex].getTripletRefId() == triplet.getRef()) {
                                    tripletHelpers[voiceIndex].getCurrentTripletSpan().setEndElement(renderer);
                                    tripletHelpers[voiceIndex].setEndColumn(currentColumn);
                                } else {
                                    // if the ref ids do not match then add the current TripletSpan to the layout.
                                    addTripletSpanToLayout(tripletHelpers[voiceIndex]);
                                    tripletHelpers[voiceIndex] = null;
                                }
                            }
                            if (tripletHelpers[voiceIndex] == null) {
                                tripletHelpers[voiceIndex] = new TripletHelper(voiceIndex, triplet.getRef(), currentColumn, new TripletSpan().setStartElement(renderer).setEndElement(renderer));
                            }
                        } else if (tripletHelpers[voiceIndex] != null) {
                            addTripletSpanToLayout(tripletHelpers[voiceIndex]);
                            tripletHelpers[voiceIndex] = null;
                        }

                        cell.addVisualToNotes(renderer);
                    }
                    if (element instanceof Rest) {
                        cell.addVisualToNotes(new RestRender((Rest)element));
                    }
                }
                add(cell, new GridConstraints(getNotesRowOfVoice(voiceIndex), currentColumn));
            }
            currentColumn++;
        }

        // check if a missing tripletHelper exists.
        for (int i = 0; i < numVoice; i++) {
            if (tripletHelpers[i] != null) {
                addTripletSpanToLayout(tripletHelpers[i]);
                tripletHelpers[i] = null;
            }
        }

        final int linesEndCol = currentColumn;

        // draw the trailing BarLines.
        for (int i = 0; i < numVoice; i++) {
            // create the ending BarLine on the right.
            Visual barLine = new BarLine().setPadding(new Rectangle2D.Float(0,0,0,0));
            // finally add the cell to the grid.
            add(barLine, new GridConstraints(getNotesRowOfVoice(i), currentColumn));
        }
        currentColumn++;

        // add the lines to the grid
        for (int i = 0; i < numVoice; i++) {
            add(new Lines(), new ColSpanGridConstraints(getNotesRowOfVoice(i), linesStartCol, linesEndCol));
        }

        // update the current cursor position.
        getXPosForTick(0);
    }

    private void addTripletSpanToLayout(final TripletHelper helper) {
        add(helper.getCurrentTripletSpan(), new ColSpanGridConstraints(getNotesRowOfVoice(helper.getVoiceIndex()), helper.getStartColumn(), helper.getEndColumn()));
    }

    private Bound getBoundDecorFromNote(Note note) {
        for (Decoration decor : note.getDecorations()) {
            if (decor instanceof Bound) {
                return (Bound)decor;
            }
        }
        return null;
    }

    private Triplet getTripletDecorFromNote(Note note) {
        for (Decoration decor : note.getDecorations()) {
            if (decor instanceof Triplet) {
                return (Triplet)decor;
            }
        }
        return null;
    }

    private int getNotesRowOfVoice(int i) {
        return i * 2 + 1;
    }

    private int getLyricsRowOfVoice(int i) {
        return i * 2 + 2;
    }

    public Dimension getPreferredSize() {
        int height = super.getPreferredSize().height;
        int width  = super.getPreferredSize().width;

        if (height > 0) {
            height += 2 * HPADDING_LINE; // add an additional padding.
        }

        if (width > 0) {
            width += 2 * VPADDING_LINE;
        }

        return new Dimension((int)(width * zoomFactor), (int)(height * zoomFactor));
    }

    public void setZoom(Zoom zoom) {
        this.zoom = zoom;
        revalidate();
    }

    public Zoom getZoom() {
        return zoom;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double newZoomFactor) {
        double old = zoomFactor;
        if (zoomFactor != newZoomFactor) {
            zoomFactor = newZoomFactor;
            firePropertyChange(ZOOM_FACTOR, old, newZoomFactor);
            revalidate();
        }
    }

    public void setSize(int width, int height) {
        /*
         * I assume that the Scrolpane calls this method to inform this canvas
         * about the possible size.
         */
        final Dimension currentPrefSize = getPreferredSize();
        double fitHeightZoom = (currentPrefSize.height == 0) ? 1 : (double)height / currentPrefSize.height;
        double fitWidthZoom  = (currentPrefSize.width  == 0) ? 1 : (double)width  / currentPrefSize.width;

        switch (zoom) {
            case FIT_TO_HEIGHT:
                setZoomFactor(zoomFactor * fitHeightZoom); break;
            case FIT_TO_WIDTH:
                setZoomFactor(zoomFactor * fitWidthZoom); break;
            case FIT_BOTH:
                setZoomFactor(zoomFactor * Math.min(fitHeightZoom, fitWidthZoom)); break;
            default: /* don not change the zoom factor */
        }

        final Dimension zoomedPrefSize = getPreferredSize();
        super.setSize(zoomedPrefSize.width, zoomedPrefSize.height);
    }

    public double getXPosForTick(long tick) {
        final TickInfo info = tickColumnMap.getInfoForTick(tick);
        if (info == null) {
            return 0;
        }

        // now i is the index of the column in which the tick is. get the x position.
        double startPosX = layout.getAbsMedianX(info.getColMin());
        double endPosX = (info.getColMax() != 0)
                ? layout.getAbsMedianX(info.getColMax() + 1)
                : layout.getAbsMedianX(info.getColMin()) + layout.getExtendRight(info.getColMin());

        final double ratio = (double)(tick - info.getTickMin()) / (info.getTickMax() - info.getTickMin());
        final double xPos = startPosX + ratio * (endPosX - startPosX) + VPADDING_LINE - LEAD_OFFSET;
        setCursorPosition(new Rectangle((int)Math.round(xPos * getZoomFactor()), HPADDING_LINE ,1, 50));
        return xPos;
    }

    public long getTickForPos(final double position) {
        double pos = position / getZoomFactor() - VPADDING_LINE + LEAD_OFFSET;
        int minCol = layout.getMinColumnIndexAtPosition(pos);
        int maxCol = layout.getMaxColumnIndexAtPosition(pos);

        double minX = layout.getAbsMedianX(minCol);
        double maxX = layout.getAbsMedianX(maxCol) + layout.getExtendRight(maxCol);
        long minTick = tickColumnMap.getInfoForColumn(minCol).getTickMin();
        long maxTick = tickColumnMap.getInfoForColumn(maxCol).getTickMax();

        double ratio = (pos - minX) / (maxX - minX);
        ratio = Math.max(ratio, 0);
        ratio = Math.min(ratio, 1);
        return Math.round(minTick + ratio * (maxTick - minTick));
    }

    public void setCursorPosition(Rectangle newCursorPosition) {
        Rectangle old = cursorPosition;
        cursorPosition = newCursorPosition;
        firePropertyChange(CURRENT_CURSOR_POSITION, old, newCursorPosition);
    }

    public Rectangle getCurrentCursorPosition() {
        return cursorPosition;
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (int)(0.3 * getScrollableBlockIncrement(visibleRect, orientation, direction));
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return orientation == SwingConstants.HORIZONTAL ? visibleRect.width : visibleRect.height;
    }

    public boolean getScrollableTracksViewportWidth() {
        return zoom == Zoom.FIT_TO_WIDTH || zoom == Zoom.FIT_BOTH;
    }

    public boolean getScrollableTracksViewportHeight() {
        return zoom == Zoom.FIT_TO_HEIGHT || zoom == Zoom.FIT_BOTH;
    }

    public enum Zoom { FIT_TO_HEIGHT, FIT_TO_WIDTH, FIT_BOTH, ZOOM }

    /** Helper class to associate a column and its render element. */
    private static class BindingHelper {
        private int column;
        private NoteRender render;

        private BindingHelper(int column, NoteRender render) {
            this.column = column;
            this.render = render;
        }

        public int getColumn() {
            return column;
        }

        public NoteRender getRender() {
            return render;
        }
    }

    /** Helper class to associate some objects together. */
    private static class TripletHelper {
        private final int voiceIndex;
        private final int tripletRefId;
        private final int startColumn;
        private int endColumn;
        private final TripletSpan currentTripletSpan;

        private TripletHelper(int voiceIndex, int tripletRefId, int startColumn, TripletSpan currentTripletSpan) {
            this.voiceIndex = voiceIndex;
            this.tripletRefId = tripletRefId;
            this.startColumn = startColumn;
            this.endColumn = startColumn;
            this.currentTripletSpan = currentTripletSpan;
        }

        public int getVoiceIndex() {
            return voiceIndex;
        }

        public int getTripletRefId() {
            return tripletRefId;
        }

        public int getStartColumn() {
            return startColumn;
        }

        public int getEndColumn() {
            return endColumn;
        }

        public void setEndColumn(int endColumn) {
            this.endColumn = endColumn;
        }

        public TripletSpan getCurrentTripletSpan() {
            return currentTripletSpan;
        }
    }
}
