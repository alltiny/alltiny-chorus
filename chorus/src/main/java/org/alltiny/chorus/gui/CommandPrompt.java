package org.alltiny.chorus.gui;

import org.alltiny.chorus.command.CommandRegistry;
import org.alltiny.chorus.model.generic.DOMPropertyChangedEvent;
import org.alltiny.chorus.model.app.ApplicationModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

/**
 * The command prompt.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @since 4.0
 */
public class CommandPrompt extends JTextField {

    private final CommandRegistry commandRegistry;

    public CommandPrompt(final ApplicationModel appModel, final CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;

        setFont(new Font("Monospaced", Font.PLAIN, 12));

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                final String text = getText();
                if (!Objects.equals(appModel.getCommandLine(), text)) {
                    appModel.setCommandLine(text);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                final String text = getText();
                if (!Objects.equals(appModel.getCommandLine(), text)) {
                    appModel.setCommandLine(text);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                final String text = getText();
                if (!Objects.equals(appModel.getCommandLine(), text)) {
                    appModel.setCommandLine(text);
                }
            }
        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    commandRegistry.execute();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        appModel.addListener((domEvent,context) -> {
            if (domEvent instanceof DOMPropertyChangedEvent) {
                DOMPropertyChangedEvent<ApplicationModel,String> pce = (DOMPropertyChangedEvent<ApplicationModel,String>)domEvent;
                if (pce.getPropertyName().equals(ApplicationModel.Property.COMMAND_LINE.name()) &&
                    !Objects.equals(pce.getNewValue(), getText())) {
                    setText(pce.getNewValue());
                }
            }
        });
    }
}
