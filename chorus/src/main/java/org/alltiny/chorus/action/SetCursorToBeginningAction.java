package org.alltiny.chorus.action;

import org.alltiny.chorus.midi.MidiPlayer;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionEvent;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 04.02.2009 21:40:16
 */
public class SetCursorToBeginningAction extends AbstractAction {

    private final MidiPlayer player;

    public SetCursorToBeginningAction(final MidiPlayer player) {
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("image/begin.png")));
        putValue(Action.SHORT_DESCRIPTION, "Zurück zum Anfang");
        this.player = player;

        player.addPropertyChangeListener(MidiPlayer.PLAYABLE, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setEnabled(player.isPlayable());
            }
        });

        // get into a valid state.
        setEnabled(player.isPlayable());
    }

    public void actionPerformed(ActionEvent e) {
        player.setTickPosition(0);
    }
}
