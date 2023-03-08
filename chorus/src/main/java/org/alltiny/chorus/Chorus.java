package org.alltiny.chorus;

import org.alltiny.chorus.command.CommandRegistry;
import org.alltiny.chorus.dom.Song;
import org.alltiny.chorus.gui.CommandPanel;
import org.alltiny.chorus.gui.canvas.AutoscrollLogic;
import org.alltiny.chorus.gui.canvas.MusicCanvas;
import org.alltiny.chorus.gui.MuteVoiceToolbar;
import org.alltiny.chorus.gui.TempoToolbar;
import org.alltiny.chorus.gui.SliderPane;
import org.alltiny.chorus.gui.ZoomToolbar;
import org.alltiny.chorus.gui.canvas.MusicLayeredPane;
import org.alltiny.chorus.action.OpenFromFileAction;
import org.alltiny.chorus.action.PlayCurrentSongAction;
import org.alltiny.chorus.action.SetCursorToBeginningAction;
import org.alltiny.chorus.model.SongMusicDataModel;
import org.alltiny.chorus.midi.MidiPlayer;
import org.alltiny.chorus.model.app.ApplicationModel;
import org.alltiny.chorus.model.generic.DOMPropertyListenerAdapter;
import org.alltiny.chorus.model.helper.ClefHelper;
import org.alltiny.chorus.util.ManifestUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Chorus main entry point.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008
 */
public class Chorus {

    private static final ManifestUtil manifest = new ManifestUtil("Chorus");
    private final ApplicationProperties properties;

    private final ApplicationModel appModel = new ApplicationModel();
    private final CommandRegistry commandRegistry = new CommandRegistry(appModel);

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

        new ClefHelper(appModel);

        player = new MidiPlayer(appModel);
        openAction = new OpenFromFileAction(commandRegistry, properties);
        playAction = new PlayCurrentSongAction(player);
        curBeginnAction = new SetCursorToBeginningAction(player);
    }

    public JPanel getContentPanel() {
        final MusicCanvas musicCanvas = new MusicCanvas(appModel, new SongMusicDataModel(appModel));

        JToolBar bar = new JToolBar();
        bar.add(new JButton(openAction));
        bar.add(new JButton(curBeginnAction));
        bar.add(new JButton(playAction));

        JPanel barPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        barPanel.add(bar);
        barPanel.add(new MuteVoiceToolbar(appModel, player));
        barPanel.add(new TempoToolbar(appModel));
        barPanel.add(new ZoomToolbar(musicCanvas));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(0,-1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);

        final SliderPane slider = new SliderPane(player);

        panel.add(barPanel, gbc);
        panel.add(slider, gbc);

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
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        JSplitPane centerSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane, new CommandPanel(appModel, commandRegistry));
        panel.add(centerSplit, gbc);

        return panel;
    }

    private ApplicationModel getAppModel() {
        return appModel;
    }

    public String getVersion() {
        return manifest.getMainAttribute("Implementation-Version", "DEV");
    }

    /**
     * This method performs last operation when shutting down the app.
     */
    public void shutdown() {
        properties.storeProperties();
    }

    public static void main(String[] args) {
        final Chorus app = new Chorus();

        final JFrame frame = new JFrame("Chorus " + app.getVersion());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        app.getAppModel().addListener(new DOMPropertyListenerAdapter<ApplicationModel,Song>(app.getAppModel(), ApplicationModel.Property.CURRENT_SONG.name()) {
            @Override
            protected void changed(Song oldValue, Song newValue) {
                if (newValue == null) {
                    frame.setTitle("Chorus " + app.getVersion());
                } else {
                    frame.setTitle("Chorus " + app.getVersion() + " - " + newValue.getAuthor() + " - " + newValue.getTitle());
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
