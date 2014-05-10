package org.alltiny.chorus.gui.canvas;

import org.alltiny.chorus.midi.MidiPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This layered pane is used to organize overlaying layer above the music canvas.
 * These layers may be used for showing cursor positions or to display highlighted
 * strokes while editing.
 */
public class MusicLayeredPane extends JLayeredPane implements Scrollable {

    private final MusicCanvas mainDelegate;
    private Scrollable scrollableDelegate = null;

    public MusicLayeredPane(MusicCanvas canvas, MidiPlayer player) {
        mainDelegate = canvas;
        scrollableDelegate = canvas;

        // add the canvas to the base layer.
        add(canvas, new Integer(0));

        // create a CurrentSliderPane
        final SliderPositionPane sliderPane = new SliderPositionPane(canvas, player);
        add(sliderPane, new Integer(1));

        // create a MousePosRenderingPane
        final MousePositionPane mousePane = new MousePositionPane();
        add(mousePane, new Integer(2));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                mousePane.setCurrentMouseXPos(null);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePane.setCurrentMouseXPos(e.getX());
            }
        });
    }

    public Dimension getPreferredSize() {
        return mainDelegate.getPreferredSize();
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        for (Component comp : getComponents()) {
            comp.setSize(width, height);
        }
    }

    @Override
    public Dimension getSize(Dimension rv) {
        return mainDelegate.getSize();
    }

    public Dimension getPreferredScrollableViewportSize() {
        return (scrollableDelegate != null) ? scrollableDelegate.getPreferredScrollableViewportSize() : null;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (scrollableDelegate != null) ? scrollableDelegate.getScrollableUnitIncrement(visibleRect, orientation, direction) : 0;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (scrollableDelegate != null) ? scrollableDelegate.getScrollableBlockIncrement(visibleRect, orientation, direction) : 0;
    }

    public boolean getScrollableTracksViewportWidth() {
        return (scrollableDelegate != null) && scrollableDelegate.getScrollableTracksViewportWidth();
    }

    public boolean getScrollableTracksViewportHeight() {
        return (scrollableDelegate != null) && scrollableDelegate.getScrollableTracksViewportHeight();
    }

    private static class MousePositionPane extends JComponent {

        private Integer currentMouseXPos = null;

        public void setCurrentMouseXPos(Integer currentMouseXPos) {
            Integer oldPos = this.currentMouseXPos;
            this.currentMouseXPos = currentMouseXPos;
            if (oldPos == null && currentMouseXPos != null) {
                repaint(currentMouseXPos - 1, 0, currentMouseXPos + 1, getSize().height);
            } else if (oldPos != null && currentMouseXPos == null) {
                repaint(oldPos - 1, 0, oldPos + 1, getSize().height);
            } else if (oldPos != null && currentMouseXPos != null) {
                int minX = Math.min(oldPos,currentMouseXPos) - 1;
                int maxX = Math.max(oldPos,currentMouseXPos) + 1;
                repaint(minX, 0, maxX - minX, getSize().height);
            }
        }

        @Override
        public void paint(Graphics g) {
            if (currentMouseXPos != null) {
                g.drawLine(currentMouseXPos, 0, currentMouseXPos, getSize().height);
            }
        }
    }

    private static class SliderPositionPane extends JComponent {

        private final MusicCanvas canvas;
        private final MidiPlayer player;

        private final double strokeWidth = 1;

        /** position of the current cursor. null means it does not exist. */
        private Double currentCursorPosX = null;

        private SliderPositionPane(final MusicCanvas canvas, final MidiPlayer player) {
            this.canvas = canvas;
            this.player = player;
            setOpaque(false);

            // bind the label to the model.
            player.addPropertyChangeListener(MidiPlayer.CURRENT_POSITION, new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    double newCursorPosX = canvas.getXPosForTick((Long)evt.getNewValue());

                    if (currentCursorPosX == null || currentCursorPosX != newCursorPosX) {
                        double oldX = Double.MAX_VALUE;
                        // add the old cursor position of the repaint region, if existing.
                        if (currentCursorPosX != null) {
                            oldX = currentCursorPosX * canvas.getZoomFactor();
                            // trigger a repaint for the region of the old current position. (for clearing)
                            //repaint(, 0, (int)Math.ceil(strokeWidth * canvas.getZoomFactor()), getSize().height);
                        }
                        // set the new position.
                        currentCursorPosX = newCursorPosX;
                        final double curX = currentCursorPosX * canvas.getZoomFactor();
                        final double stroke = strokeWidth * canvas.getZoomFactor();
                        // trigger a repaint for the region spanning over of the old and new current position.
                        int xMin = (int)Math.floor(Math.min(oldX, curX) - stroke);
                        int xMax = (int)Math.ceil(Math.max(curX,oldX) + stroke);
                        repaint(xMin, 0, xMax - xMin, getSize().height);
                    }
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            if (currentCursorPosX != null) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(new Color(1f, 0f, 0f, 0.7f));
                g2d.setStroke(new BasicStroke((float)strokeWidth));
                g2d.scale(canvas.getZoomFactor(), 1);
                g2d.drawLine((int)Math.round(currentCursorPosX), 0, (int)Math.round(currentCursorPosX), getSize().height);
            }
        }
    }
}
