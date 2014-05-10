package org.alltiny.chorus.gui;

import org.alltiny.chorus.midi.MidiPlayer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 21.10.2009 19:38:17
 */
public class SliderPane extends JPanel {

    private final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");
    private final CurrentPositionSlider slider;

    public SliderPane(MidiPlayer player) {
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths  = new int[]{70,70,70};
        gbl.columnWeights = new double[]{0,1,0};
        setLayout(gbl);

        GridBagConstraints gbc = new GridBagConstraints(-1,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(1,2,1,2),0,0);

        final JLabel currentTime = new JLabel();
        final JLabel absoluteTime = new JLabel();

        slider = new CurrentPositionSlider(player);

        add(currentTime, gbc);
        add(slider, gbc);
        add(absoluteTime, gbc);

        // bind the label to the model.
        player.addPropertyChangeListener(MidiPlayer.CURRENT_MILLISEC, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis((long)((Long)evt.getNewValue() * 0.001));
                currentTime.setText(sdf.format(cal.getTime()));
            }
        });
        player.addPropertyChangeListener(MidiPlayer.ABSOLUTE_MILLISEC, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis((long)((Long)evt.getNewValue() * 0.001));
                absoluteTime.setText(sdf.format(cal.getTime()));
            }
        });
    }

    public void setRangeWidth(int width) {
        slider.setRangeWidth(width);
    }

    public void setLeftRange(int left) {
        slider.setLeftRange(left);
    }

    public void setRightRange(int right) {
        slider.setRightRange(right);
    }
}
