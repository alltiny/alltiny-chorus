package org.alltiny.chorus.gui;

import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.alltiny.chorus.model.generic.Context;
import org.alltiny.chorus.model.generic.DOMHierarchicalListener;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 25.09.2009 18:29:39
 */
public class TempoToolbar extends JToolBar {

    public TempoToolbar(final ApplicationModel model) {
        final JComboBox<Float> tempo = new JComboBox<>(new Float[]{4f, 2f, 1.5f, 1f, 0.75f, 0.5f, 0.25f});
        tempo.setSelectedItem(1f);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = -1;
        gbc.ipadx = 5;
        add(new JLabel(ResourceBundle.getBundle("i18n.chorus").getString("TempoToolbar.Label")), gbc);
        add(tempo, gbc);

        model.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.CURRENT_SONG.name()),
                new DOMHierarchicalListener.Callback<Song,String>() {
                    @Override
                    public void added(Song song, String property, Context<?> context) {
                        setVisible(song != null);
                    }

                    @Override
                    public void changed(Song song, String property, Context<?> context) {}

                    @Override
                    public void removed(Song song, String property, Context<?> context) {
                        setVisible(false);
                    }
                }).setName(getClass().getSimpleName() + "@SONG"));

        model.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(ApplicationModel.class, ApplicationModel.Property.TEMPO_FACTOR.name()),
                new DOMHierarchicalListener.Callback<Float,String>() {
                    @Override
                    public void added(Float factor, String property, Context<?> context) {
                        tempo.setSelectedItem(factor != null ? factor : 1);
                    }

                    @Override
                    public void changed(Float factor, String property, Context<?> context) {
                        tempo.setSelectedItem(factor != null ? factor : 1);
                    }

                    @Override
                    public void removed(Float factor, String property, Context<?> context) {
                        tempo.setSelectedItem(1);
                    }
                }).setName(getClass().getSimpleName() + "@TEMPO_FACTOR"));

        tempo.addItemListener(e -> {
            // only process selected events.
            if (e.getStateChange() == ItemEvent.SELECTED) {
                model.setTempoFactor((Float)e.getItem());
            }
        });
    }
}
