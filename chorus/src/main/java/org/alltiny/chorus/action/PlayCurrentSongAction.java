package org.alltiny.chorus.action;

import org.alltiny.chorus.midi.MidiPlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 04.02.2009 21:40:16
 */
public class PlayCurrentSongAction extends AbstractAction {

    private static final Icon PLAY_ICON = new ImageIcon(PlayCurrentSongAction.class.getClassLoader().getResource("image/play.png"));
    private static final Icon PAUSE_ICON = new ImageIcon(PlayCurrentSongAction.class.getClassLoader().getResource("image/pause.png"));
    private static final String PLAY_DESCR = "Abspielen";
    private static final String PAUSE_DESCR = "Pausieren";

    private final MidiPlayer player;

    public PlayCurrentSongAction(final MidiPlayer player) {
        putValue(Action.SMALL_ICON, PAUSE_ICON);
        putValue(Action.SHORT_DESCRIPTION, PLAY_DESCR);

        this.player = player;

        player.addPropertyChangeListener(MidiPlayer.PLAYABLE, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setEnabled(player.isPlayable());
            }
        });
        player.addPropertyChangeListener(MidiPlayer.RUNNING, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (player.isRunning()) {
                    putValue(Action.SMALL_ICON, PAUSE_ICON);
                    putValue(Action.SHORT_DESCRIPTION, PAUSE_DESCR);
                } else {
                    putValue(Action.SMALL_ICON, PLAY_ICON);
                    putValue(Action.SHORT_DESCRIPTION, PLAY_DESCR);
                }
            }
        });

        // get into a valid state.
        setEnabled(player.isPlayable());
    }

    public void actionPerformed(ActionEvent e) {
        if (player.isRunning()) {
            player.stop();
        } else {
            player.start();
        }
    }
}
