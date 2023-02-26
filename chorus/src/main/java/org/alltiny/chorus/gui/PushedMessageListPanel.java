package org.alltiny.chorus.gui;

import org.alltiny.chorus.model.app.ApplicationModel;

import javax.swing.*;
import java.awt.*;

/**
 * This panel renders the application messages as {@link MessagePanel}
 * and keeps track of changes in the application message queue.
 */
public class PushedMessageListPanel extends JPanel implements Scrollable {

    private final JPanel pusherPanel;
    private final MessageListPanel listPanel;

    public PushedMessageListPanel(ApplicationModel appModel) {
        super(new BorderLayout());

        pusherPanel = new JPanel();
        pusherPanel.setPreferredSize(new Dimension(0, 0));
        pusherPanel.setBackground(new Color(224, 224, 224));
        listPanel = new MessageListPanel(appModel);

        add(pusherPanel);
        add(listPanel, BorderLayout.SOUTH);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 3;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            return (int)visibleRect.getHeight();
        } else {
            return (int)visibleRect.getWidth();
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        int viewportHeight = getParent().getSize().height;
        int listHeight = listPanel.getPreferredSize().height;
        return viewportHeight > listHeight;
    }
}
