package org.alltiny.chorus.gui.canvas;

import org.alltiny.base.model.PropertySupportBean;
import org.alltiny.chorus.midi.MidiPlayer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This model instance calculates the current slider position for the corresponding tick.
 * This class was introduce to minimized rendering efforts: while the tick changes a lot,
 * the slider must only be re-rendered if its position is changed by one pixel or more.
 * This class calculates the pixel position and fires a change event if the slider must be
 * rendered on a new position.
 */
public class SliderPositionModel extends PropertySupportBean {

    public static final String SLIDER_POSITION = "sliderPosition";

    private double canvasZoomFactor;
    private double cursorPositionForTick;

    private int sliderPosition = 0;

    public SliderPositionModel(final MusicCanvas canvas, final MidiPlayer player) {
        // initialize values.
        canvasZoomFactor = canvas.getZoomFactor();
        cursorPositionForTick = canvas.getXPosForTick(player.getTickPosition());
        // bind listeners to the component to get informed about changes.
        player.addPropertyChangeListener(MidiPlayer.CURRENT_POSITION, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                cursorPositionForTick = canvas.getXPosForTick((Long)evt.getNewValue());
                calculateSliderPosition();
            }
        });
        canvas.addPropertyChangeListener(MusicCanvas.ZOOM_FACTOR, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                canvasZoomFactor = (Double)evt.getNewValue();
                calculateSliderPosition();
            }
        });
        // get into proper initial state.
        calculateSliderPosition();
    }

    private void calculateSliderPosition() {
        int oldSliderPosition = sliderPosition;
        sliderPosition = (int)Math.round(cursorPositionForTick * canvasZoomFactor);
        firePropertyChange(SLIDER_POSITION, oldSliderPosition, sliderPosition);
    }

    public int getSliderPosition() {
        return sliderPosition;
    }
}
