package org.alltiny.chorus.gui.canvas;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This logic implements auto-scrolling. It has two modes:
 *  (1) if the cursor reaches a region on the right side, then scrolling to right is performed.
 *  (2) if the cursor if completely out of the viewport, then the viewport scrolls to cursor that the cursor is in the middle of the view port.
 */
public class AutoscrollLogic {

    public AutoscrollLogic(final JScrollPane pane, final MusicCanvas canvas) {
        canvas.addPropertyChangeListener(MusicCanvas.CURRENT_CURSOR_POSITION, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Rectangle cursor = canvas.getCurrentCursorPosition();

                final int rightBound = (int)(0.1 * pane.getSize().width);

                // check if the cursor is out of bounds on the right side.
                if ((int)cursor.getMaxX() + rightBound > pane.getViewport().getViewRect().getMaxX() && (int)cursor.getMaxX() <= pane.getViewport().getViewRect().getMaxX()) {
                    // only scroll right if the viewport isn't already on the right side.
                    if (pane.getViewport().getViewRect().getMaxX() < pane.getViewport().getViewSize().width) {
                        Point current = pane.getViewport().getViewPosition();
                        current.x = cursor.x - (int)(0.1 * pane.getViewport().getViewRect().width);
                        pane.getViewport().setViewPosition(current);
                    }
                } else if ((int)cursor.getMaxX() > pane.getViewport().getViewRect().getMaxX() || (int)cursor.getMinX() < pane.getViewport().getViewRect().getMinX()) { // check if the cursor is out of bounds on the left side.
                    // place the cursor in the middle of the screen.
                    Point current = pane.getViewport().getViewPosition();
                    current.x = cursor.x - (int)(0.45 * pane.getViewport().getViewRect().width);
                    pane.getViewport().setViewPosition(current);
                }
            }
        });
    }
}
