package org.alltiny.chorus.gui;

import org.alltiny.chorus.command.CommandRegistry;
import org.alltiny.chorus.model.app.ApplicationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CommandPanel extends JPanel {

    private int lastScrollToY = -1;

    public CommandPanel(final ApplicationModel appModel, final CommandRegistry commandRegistry) {
        super(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(new PushedMessageListPanel(appModel));

        scrollPane.getViewport().getView().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final int scrollToY = e.getComponent().getSize().height;
                if (lastScrollToY != scrollToY) {
                    scrollPane.getViewport().scrollRectToVisible(new Rectangle(0, scrollToY, 1, 1));
                    lastScrollToY = scrollToY;
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(new CommandPrompt(appModel, commandRegistry), BorderLayout.SOUTH);
    }
}
