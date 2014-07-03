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
        final SliderPositionPane sliderPane = new SliderPositionPane(canvas, new SliderPositionModel(canvas, player));
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
        private final double strokeWidth = 1;

        /** position of the current cursor. null means it does not exist. */
        private Integer sliderPosition = null;

        private SliderPositionPane(final MusicCanvas canvas, final SliderPositionModel sliderPositionModel) {
            this.canvas = canvas;
            setOpaque(false);

            // bind the listeners to the model.
            sliderPositionModel.addPropertyChangeListener(SliderPositionModel.SLIDER_POSITION, new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    final double stroke2 = 0.5 * strokeWidth * canvas.getZoomFactor();
                    int xMin = Integer.MAX_VALUE;
                    int xMax = Integer.MIN_VALUE;
                    if (sliderPosition != null) {
                        xMin = (int)Math.floor(sliderPosition - stroke2);
                        xMax = (int)Math.ceil(sliderPosition + stroke2);
                    }
                    sliderPosition = (Integer)evt.getNewValue();
                    if (sliderPosition != null) {
                        xMin = Math.min(xMin, (int)Math.floor(sliderPosition - stroke2));
                        xMax = Math.max(xMax, (int)Math.ceil(sliderPosition + stroke2));
                    }
                    // trigger a repaint for the region spanning over of the old and new current position.
                    repaint(xMin, 0, xMax - xMin, getSize().height);
                }
            });
            canvas.addPropertyChangeListener(MusicCanvas.ZOOM_FACTOR, new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (sliderPosition != null) {
                        final double stroke = strokeWidth * canvas.getZoomFactor();
                        int xMin = (int)Math.floor(sliderPosition - stroke);
                        int xMax = (int)Math.ceil(sliderPosition + stroke);
                        // trigger a repaint for the region spanning over of the old and new current position.
                        repaint(xMin, 0, xMax - xMin, getSize().height);
                    }
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            if (sliderPosition != null) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(new Color(1f, 0f, 0f, 0.7f));
                g2d.setStroke(new BasicStroke((float)(strokeWidth*canvas.getZoomFactor())));
                g2d.drawLine(sliderPosition, 0, sliderPosition, getSize().height);
            }
        }
    }
}
