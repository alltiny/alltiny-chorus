package org.alltiny.chorus.gui;

import org.alltiny.chorus.model.SongModel;
import org.alltiny.chorus.midi.MidiPlayer;
import org.alltiny.chorus.dom.Voice;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 25.09.2009 18:29:39
 */
public class MuteVoiceToolbar extends JToolBar {

    private final SongModel model;
    private final MidiPlayer player;

    public MuteVoiceToolbar(final SongModel model, MidiPlayer player) {
        this.model = model;
        this.player = player;

        model.addPropertyChangeListener(SongModel.CURRENT_SONG, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // remove all buttons.
                MuteVoiceToolbar.this.removeAll();

                boolean atLeastOneButton = false;
                // create new un-/mute button for each voice.
                if (model.getSong() != null) {
                    int i = 0;
                    for (Voice voice : model.getSong().getMusic().getVoices()) {
                        JToggleButton button = new JToggleButton(voice.getName(), true);
                        button.addItemListener(new MutingItemListener(i));
                        MuteVoiceToolbar.this.add(button);
                        i++;
                        atLeastOneButton = true;
                    }
                }
                setVisible(atLeastOneButton);
            }
        });

        setVisible(false);
    }

    /** This listener mute the right midi track. */
    private class MutingItemListener implements ItemListener {
        private final int index;

        public MutingItemListener(int index) {
            this.index = index;
        }

        public void itemStateChanged(ItemEvent e) {
            MuteVoiceToolbar.this.player.setTrackMute(index, e.getStateChange() == ItemEvent.DESELECTED);
        }
    }
}
