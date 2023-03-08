package org.alltiny.chorus.action;

import org.alltiny.chorus.ApplicationProperties;
import org.alltiny.chorus.command.CommandRegistry;
import org.alltiny.chorus.command.OpenFileCommand;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 04.02.2009 21:40:16
 */
public class OpenFromFileAction extends AbstractAction {

    private static final String PROP_LAST_OPEN_DIR = "OpenFromFileAction.lastOpenDirectory";

    private final CommandRegistry commandRegistry;
    private final JFileChooser chooser;
    private final ApplicationProperties properties;

    public OpenFromFileAction(CommandRegistry commandRegistry, ApplicationProperties properties) {
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("image/open.png")));
        putValue(Action.SHORT_DESCRIPTION, ResourceBundle.getBundle("i18n.chorus").getString("OpenFromFileAction.ShortDescription"));
        this.commandRegistry = commandRegistry;
        this.properties = properties;

        chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() ||
                       f.getName().toLowerCase().endsWith(".xml") ||
                       f.getName().toLowerCase().endsWith(".xml.gz");
            }

            public String getDescription() {
                return "*.xml, *.xml.gz";
            }
        });

        // set the open path from the properties if existing.
        if (properties.getProperty(PROP_LAST_OPEN_DIR) != null) {
            chooser.setCurrentDirectory(new File(properties.getProperty(PROP_LAST_OPEN_DIR)));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
            try {
                commandRegistry.execute(OpenFileCommand.commandFor(chooser.getSelectedFile()));

                // store the current directory in the properties.
                properties.setProperty(PROP_LAST_OPEN_DIR, chooser.getCurrentDirectory().getAbsolutePath());
            } catch (Exception ex) {
                System.out.println("Error reading file '" + chooser.getSelectedFile().getAbsolutePath() + "':" + ex);
                ex.printStackTrace();
            }
        }
    }
}
