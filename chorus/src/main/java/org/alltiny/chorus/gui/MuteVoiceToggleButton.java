package org.alltiny.chorus.gui;

import org.alltiny.chorus.dom.Voice;
import org.alltiny.chorus.model.generic.Context;
import org.alltiny.chorus.model.generic.DOMHierarchicalListener;

import javax.swing.*;
import java.awt.event.ItemEvent;

/**
 * This toggle button registers on a voice.
 * Toggling it will mute/unmute the voice.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.03.2023
 */
public class MuteVoiceToggleButton extends JToggleButton {

    private final Voice voice;

    public MuteVoiceToggleButton(final Voice voice) {
        this.voice = voice;

        voice.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Voice.class, Voice.Property.MUTED.name()),
                new DOMHierarchicalListener.Callback<Boolean,String>() {
                    @Override
                    public void added(Boolean muted, String property, Context<?> context) {
                        MuteVoiceToggleButton.this.setSelected(muted == null || !muted);
                    }

                    @Override
                    public void changed(Boolean muted, String property, Context<?> context) {
                        MuteVoiceToggleButton.this.setSelected(muted == null || !muted);
                    }

                    @Override
                    public void removed(String property, Context<?> context) {
                        MuteVoiceToggleButton.this.setSelected(true);
                    }
                }).setName(getClass().getSimpleName() + "@MUTED"));

        voice.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Voice.class, Voice.Property.NAME.name()),
                new DOMHierarchicalListener.Callback<String,String>() {
                    @Override
                    public void added(String name, String property, Context<?> context) {
                        MuteVoiceToggleButton.this.setText(name);
                    }

                    @Override
                    public void changed(String name, String property, Context<?> context) {
                        MuteVoiceToggleButton.this.setText(name);
                    }

                    @Override
                    public void removed(String property, Context<?> context) {
                        MuteVoiceToggleButton.this.setText("");
                    }
                }).setName(getClass().getSimpleName() + "@NAME"));

        addItemListener(e -> voice.setMuted(e.getStateChange() == ItemEvent.DESELECTED));
    }
}
