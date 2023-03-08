package org.alltiny.chorus.gui;

import org.alltiny.chorus.dom.Music;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.midi.MidiPlayer;
import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.alltiny.chorus.model.generic.Context;
import org.alltiny.chorus.model.generic.DOMHierarchicalListener;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 25.09.2009 18:29:39
 */
public class MuteVoiceToolbar extends JToolBar {

    private final ApplicationModel model;
    private final MidiPlayer player;

    public MuteVoiceToolbar(final ApplicationModel model, MidiPlayer player) {
        this.model = model;
        this.player = player;

        setVisible(false);

        model.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.CURRENT_SONG.name()),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Song.class, Song.Property.MUSIC.name()),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Music.class, Music.Property.VOICES.name()),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.AnyItemInList<>(),
                new DOMHierarchicalListener.Callback<Voice,Integer>() {
                    @Override
                    public void added(Voice voice, Integer identifier, Context<?> context) {
                        MuteVoiceToolbar.this.add(new MuteVoiceToggleButton(voice), identifier);
                        setVisible(true);
                    }

                    @Override
                    public void changed(Voice value, Integer identifier, Context<?> context) {}

                    @Override
                    public void removed(Integer identifier, Context<?> context) {
                        MuteVoiceToolbar.this.remove(identifier);
                        setVisible(MuteVoiceToolbar.this.getComponents().length > 0);
                    }
                })
            ))).setName(getClass().getSimpleName()));
    }

    private void removeAllButtons() {
        removeAll();
    }

    private void initialize() {
        removeAll();

        boolean atLeastOneButton = false;
        // create new un-/mute button for each voice.
        if (model.getCurrentSong() != null) {
            int i = 0;
            for (Voice voice : model.getCurrentSong().getMusic().getVoices()) {
                JToggleButton button = new JToggleButton(voice.getName(), true);
                button.addItemListener(new MutingItemListener(i));
                MuteVoiceToolbar.this.add(button);
                i++;
                atLeastOneButton = true;
            }
        }
        setVisible(atLeastOneButton);
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
