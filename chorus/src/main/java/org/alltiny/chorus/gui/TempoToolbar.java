package org.alltiny.chorus.gui;

import org.alltiny.chorus.model.SongModel;
import org.alltiny.chorus.midi.MidiPlayer;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.*;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 25.09.2009 18:29:39
 */
public class TempoToolbar extends JToolBar {

    public TempoToolbar(final SongModel model, final MidiPlayer player) {
        final JComboBox tempo = new JComboBox(new Float[]{4f, 2f, 1.5f, 1f, 0.75f, 0.5f, 0.25f});
        tempo.setSelectedItem(1f);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = -1;
        gbc.ipadx = 5;
        add(new JLabel("Tempo"), gbc);
        add(tempo, gbc);

        model.addPropertyChangeListener(SongModel.CURRENT_SONG, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setVisible(model.getSong() != null);
                // if a new song is selected then reset the tempo to 1.0
                tempo.setSelectedItem(1f);
            }
        });

        tempo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // only process selected events.
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    player.setTempoFactor((Float)e.getItem());
                }
            }
        });

        setVisible(model.getSong() != null);
    }
}
