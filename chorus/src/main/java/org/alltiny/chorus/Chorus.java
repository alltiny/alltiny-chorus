package org.alltiny.chorus;

import org.alltiny.chorus.gui.canvas.AutoscrollLogic;
import org.alltiny.chorus.gui.canvas.MusicCanvas;
import org.alltiny.chorus.gui.MuteVoiceToolbar;
import org.alltiny.chorus.gui.TempoToolbar;
import org.alltiny.chorus.gui.SliderPane;
import org.alltiny.chorus.gui.ZoomToolbar;
import org.alltiny.chorus.gui.canvas.MusicLayeredPane;
import org.alltiny.chorus.model.SongModel;
import org.alltiny.chorus.model.SongModelFactory;
import org.alltiny.chorus.action.OpenFromFileAction;
import org.alltiny.chorus.action.PlayCurrentSongAction;
import org.alltiny.chorus.action.SetCursorToBeginningAction;
import org.alltiny.chorus.model.SongMusicDataModel;
import org.alltiny.chorus.midi.MidiPlayer;
import org.alltiny.svg.parser.SVGParseException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:56:31
 */
public class Chorus {

    private final ApplicationProperties properties;

    private final SongModel model;
    private final OpenFromFileAction openAction;
    private final PlayCurrentSongAction playAction;
    private final SetCursorToBeginningAction curBeginnAction;

    private final MidiPlayer player;

    public Chorus() {
        /*try {
            SynthLookAndFeel laf = new SynthLookAndFeel();
            InputStream in = getClass().getClassLoader().getResourceAsStream("resource/laf.xml");
            laf.load(in, getClass());
            UIManager.setLookAndFeel(laf);
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }*/
        properties = new ApplicationProperties(new File(System.getProperty("user.home") + System.getProperty("file.separator") +  ".Chorus", "defaults.ini"));

        model = SongModelFactory.createInstance();
        player = new MidiPlayer(model);
        openAction = new OpenFromFileAction(model, properties);
        playAction = new PlayCurrentSongAction(player);
        curBeginnAction = new SetCursorToBeginningAction(player);
    }

    public JPanel getContentPanel() throws IOException, SVGParseException {
        final MusicCanvas musicCanvas = new MusicCanvas(model, new SongMusicDataModel(model));

        JToolBar bar = new JToolBar();
        bar.add(new JButton(openAction));
        bar.add(new JButton(curBeginnAction));
        bar.add(new JButton(playAction));

        JPanel barPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        barPanel.add(bar);
        barPanel.add(new MuteVoiceToolbar(model, player));
        barPanel.add(new TempoToolbar(model, player));
        barPanel.add(new ZoomToolbar(musicCanvas));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(0,-1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);

        final SliderPane slider = new SliderPane(player);

        panel.add(barPanel, gbc);
        panel.add(slider, gbc);
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane pane = new JScrollPane(new MusicLayeredPane(musicCanvas, player));

        new AutoscrollLogic(pane, musicCanvas);

        musicCanvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                player.setTickPosition(musicCanvas.getTickForPos(e.getX()));
            }
        });

        // register a lister at the pane to get informed about changes of the viewport.
        pane.getViewport().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JViewport) {
                    JViewport port = (JViewport)e.getSource();
                    slider.setRangeWidth(port.getViewSize().width);
                    slider.setLeftRange(port.getViewPosition().x);
                    slider.setRightRange(port.getViewPosition().x + port.getExtentSize().width);
                }
            }
        });
        panel.add(pane, gbc);

        return panel;
    }

    private SongModel getModel() {
        return model;
    }

    public String getVersion() {
        return "0.2.4 (alpha)";
    }

    /**
     * This method performs last operation when shutting down the app.
     */
    public void shutdown() {
        properties.storeProperties();
    }

    public static void main(String[] args) throws IOException, SVGParseException {
        final Chorus app = new Chorus();

        final JFrame frame = new JFrame("Chorus " + app.getVersion());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        app.getModel().addPropertyChangeListener(SongModel.CURRENT_SONG, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (app.getModel().getSong() == null) {
                    frame.setTitle("Chorus " + app.getVersion());
                } else {
                    frame.setTitle("Chorus " + app.getVersion() + " - " + app.getModel().getSong().getAuthor() + " - " + app.getModel().getSong().getTitle());
                }
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                app.shutdown();
            }
        });
        //frame.getContentPane().add(new JScrollPane(new MusicCanvas(song)), BorderLayout.CENTER);

        frame.getContentPane().add(app.getContentPanel(), BorderLayout.CENTER);
        frame.setSize(720,480);
        frame.setVisible(true);
    }
}
