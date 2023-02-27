package org.alltiny.chorus.gui;

import org.alltiny.chorus.generic.model.events.DOMIndexedItemChangedEvent;
import org.alltiny.chorus.generic.model.events.DOMIndexedItemInsertedEvent;
import org.alltiny.chorus.generic.model.events.DOMIndexedItemRemovedEvent;
import org.alltiny.chorus.generic.model.events.DOMListClearedEvent;
import org.alltiny.chorus.model.app.AppMessage;
import org.alltiny.chorus.model.app.ApplicationModel;

import javax.swing.*;
import java.awt.*;

/**
 * This panel renders the application messages as {@link MessagePanel}
 * and keeps track of changes in the application message queue.
 */
public class MessageListPanel extends JPanel implements Scrollable {

    private static final GridBagConstraints GBC = new GridBagConstraints(0,-1,1,1,1,0,GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);

    private final ApplicationModel appModel;

    public MessageListPanel(ApplicationModel appModel) {
        super(new GridBagLayout());
        this.appModel = appModel;

        // initialize to all messages in the queue
        for (AppMessage message : appModel.getApplicationMessageQueue()) {
            add(new MessagePanel(message), GBC);
        }

        // register listeners
        appModel.getApplicationMessageQueue().addListener(domEvent -> {
            if (domEvent.getSource() != appModel.getApplicationMessageQueue()) {
                return;
            }
            if (domEvent instanceof DOMListClearedEvent) {
                this.removeAll();
            } else if (domEvent instanceof DOMIndexedItemChangedEvent) {
                final DOMIndexedItemChangedEvent<?,AppMessage> itemEvent = (DOMIndexedItemChangedEvent<?,AppMessage>)domEvent;
                this.remove(itemEvent.getIndex());
                this.add(new MessagePanel(itemEvent.getItem()), GBC, itemEvent.getIndex());
            } else if (domEvent instanceof DOMIndexedItemInsertedEvent) {
                final DOMIndexedItemInsertedEvent<?,AppMessage> itemEvent = (DOMIndexedItemInsertedEvent<?,AppMessage>)domEvent;
                this.add(
                    new MessagePanel(itemEvent.getItem()),
                    GBC,
                    (itemEvent.getIndex() < this.getComponentCount()) ? itemEvent.getIndex() : -1);
            } else if (domEvent instanceof DOMIndexedItemRemovedEvent) {
                final DOMIndexedItemRemovedEvent<?,AppMessage> itemEvent = (DOMIndexedItemRemovedEvent<?,AppMessage>)domEvent;
                // ignore this event if the cause is the DOMListClearedEvent as it has been handled already
                if (itemEvent.getCause() instanceof DOMListClearedEvent &&
                    itemEvent.getCause().getSource() == appModel.getApplicationMessageQueue()) {
                    return;
                }
                this.remove(itemEvent.getIndex());
            } else {
                return; // skip the revalidation of this container.
            }
            revalidate();
        });
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
        return false;
    }
}
