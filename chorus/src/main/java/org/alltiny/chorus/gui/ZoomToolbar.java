package org.alltiny.chorus.gui;

import org.alltiny.chorus.gui.canvas.MusicCanvas;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 25.09.2009 18:29:39
 */
public class ZoomToolbar extends JToolBar {

    public ZoomToolbar(final MusicCanvas canvas) {
        JToggleButton fitToHeight = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("image/fitHeight.png")), canvas.getZoom() == MusicCanvas.Zoom.FIT_TO_HEIGHT);
        JToggleButton fitToWidth  = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("image/fitWidth.png")), canvas.getZoom() == MusicCanvas.Zoom.FIT_TO_WIDTH);
        JToggleButton fitToBoth   = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("image/fitBoth.png")), canvas.getZoom() == MusicCanvas.Zoom.FIT_BOTH);
        JToggleButton freeZoom    = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("image/fitZoom.png")), canvas.getZoom() == MusicCanvas.Zoom.ZOOM);
        final JSpinner zoom = new JSpinner(new AbstractSpinnerModel() {
            private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
            public Object getValue() {
                return decimalFormat.format(canvas.getZoomFactor());
            }
            public void setValue(Object value) {
                try {
                    canvas.setZoomFactor(decimalFormat.parse((String)value).doubleValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fireStateChanged();
            }
            public Object getNextValue() {
                return decimalFormat.format(canvas.getZoomFactor() * 1.25);
            }
            public Object getPreviousValue() {
                return decimalFormat.format(canvas.getZoomFactor() * 0.8);
            }
        });
        zoom.setPreferredSize(new Dimension(50,20));
        zoom.setEnabled(canvas.getZoom() == MusicCanvas.Zoom.ZOOM);

        fitToHeight.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    canvas.setZoom(MusicCanvas.Zoom.FIT_TO_HEIGHT);
                }
            }
        });
        fitToWidth.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    canvas.setZoom(MusicCanvas.Zoom.FIT_TO_WIDTH);
                }
            }
        });
        fitToBoth.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    canvas.setZoom(MusicCanvas.Zoom.FIT_BOTH);
                }
            }
        });
        freeZoom.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    canvas.setZoom(MusicCanvas.Zoom.ZOOM);
                    zoom.setEnabled(true);
                } else {
                    zoom.setEnabled(false);
                }
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(fitToHeight);
        group.add(fitToWidth);
        group.add(fitToBoth);
        group.add(freeZoom);

        add(fitToHeight);
        add(fitToWidth);
        add(fitToBoth);
        add(freeZoom);
        add(zoom);
    }
}
