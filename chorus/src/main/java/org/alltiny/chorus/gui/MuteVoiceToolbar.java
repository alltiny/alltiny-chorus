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

    public MuteVoiceToolbar(final ApplicationModel model) {
        this.model = model;

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
                        MuteVoiceToolbar.this.add(new MuteVoiceToggleButton(voice), (int)identifier);
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
}
