package org.alltiny.chorus.gui;

import org.alltiny.chorus.midi.MidiPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * This slider implementation shows two different information:
 *  - a current value between a minimum and maximum (like a JSlider)
 *  - and a range which values are currently displayed in the main rendering view.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 04.10.2009 11:30:59
 */
public class CurrentPositionSlider extends JComponent {

    private long minValue = 0;
    private long maxValue = 100;
    private long rangeWidth = 100;
    private int currentPos = 0; // this is where the current cursor position is.
    private int leftPos = 0; // this is where the left range mark is.
    private int rightPos = 0; // this is where the right range mark is.

    public CurrentPositionSlider(final MidiPlayer player) {
        setEnabled(player.isPlayable());

        player.addPropertyChangeListener(MidiPlayer.PLAYABLE, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setEnabled(player.isPlayable());
            }
        });

        player.addPropertyChangeListener(MidiPlayer.ABSOLUTE_LENGTH, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setMaxValue((int)player.getAbsoluteLength());
            }
        });

        player.addPropertyChangeListener(MidiPlayer.CURRENT_POSITION, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setCurrentValue(((Long)evt.getNewValue()).intValue());
            }
        });

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                long value = (long)((double)e.getX() / getWidth() * (maxValue - minValue));
                player.setTickPosition(value);
            }
        });
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setRangeWidth(long width) {
        rangeWidth = width;
    }

    public void setLeftRange(long minRange) {
        // calculate the new current position.
        final int newLeftPos = (int)((double)getWidth() * minRange / rangeWidth);
        // trigger a repaint, if the current position has changed.
        if (leftPos != newLeftPos) {
            leftPos = newLeftPos;
            repaint();
        }
    }

    public void setRightRange(long maxRange) {
        // calculate the new current position.
        final int newRightPos = (int)((double)getWidth() * maxRange / rangeWidth);
        // trigger a repaint, if the current position has changed.
        if (rightPos != newRightPos) {
            rightPos = newRightPos;
            repaint();
        }
    }

    public void setCurrentValue(int currentValue) {
        // calculate the new current position.
        final int newCurrentPos = (int)((double)getWidth() * (currentValue - minValue) / (maxValue - minValue));
        // trigger a repaint, if the current position has changed.
        if (currentPos != newCurrentPos) {
            currentPos = newCurrentPos;
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g.create();
        if (isEnabled()) {
            // clear the component's rectangle.
            g2d.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
            g2d.clearRect(0, 0, getWidth(), getHeight());
            // draw the range marks.
            g2d.setColor(new Color(0.3f, 0.3f, 0.3f, 1f));
            g2d.fillRect(0, 0, leftPos, getHeight());
            g2d.fillRect(rightPos, 0, getWidth(), getHeight());
            // draw a translucent rectangle which covers the values lesser then the current value.
            g2d.setColor(new Color(1f, 0f, 0f, 0.5f));
            g2d.fillRect(0, 0, currentPos, getHeight());
            // draw the cursor itself.
            g2d.setColor(new Color(1f, 0f, 0f, 1f));
            g2d.drawLine(currentPos, 0, currentPos, getHeight());
        } else {
            g2d.setColor(new Color(0.5f, 0.5f, 0.5f, 1f));
            g2d.drawRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(40, 16);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(0, 16);
    }
}
