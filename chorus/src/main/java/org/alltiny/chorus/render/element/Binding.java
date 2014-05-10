package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class renders a binding from a beginning note to a ending note.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 18.05.2010 18:02:00
 */
public class Binding extends Visual {

    private GeneralPath path = null;

    private NoteRender startElement = null;
    private NoteRender endElement = null;

    private final PropertyChangeListener listener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            update();
        }
    };

    /**
     * Use this method to set the visual element, where this lines should start.
     * @return this following the fluent interface pattern
     */
    public Binding setStartElement(NoteRender startElement) {
        if (this.startElement != null) {
            startElement.removePropertyChangeListener(listener);
        }
        this.startElement = startElement;
        if (this.startElement != null) {
            startElement.addPropertyChangeListener(listener);
        }
        update();
        return this; // fluent interface support
    }

    /**
     * Use this method to set the visual element, where this lines should end.
     * @return this following the fluent interface pattern
     */
    public Binding setEndElement(NoteRender endElement) {
        if (this.endElement != null) {
            endElement.removePropertyChangeListener(listener);
        }
        this.endElement = endElement;
        if (this.endElement != null) {
            endElement.addPropertyChangeListener(listener);
        }
        update();
        return this; // fluent interface support.
    }

    private void update() {
        path = null;
        if (startElement != null && endElement != null) {
            path = new GeneralPath();
            double sX = startElement.getAbsMedianX();
            double sY = startElement.getAbsMedianY() - startElement.getRelativePosY() * 0.5 * LINES_SPACE;
            double eX = endElement.getAbsMedianX();
            double eY = endElement.getAbsMedianY() - endElement.getRelativePosY() * 0.5 * LINES_SPACE;
            double ratio = (eX - sX) / (10 * LINES_SPACE);
            // draw four rectangles as the corners of the double path.
            path.append(new CubicCurve2D.Double(
                sX,sY,
                sX + ratio * 2 * LINES_SPACE, sY + ratio * 2 * LINES_SPACE,
                eX - ratio * 2 * LINES_SPACE, eY + ratio * 2 * LINES_SPACE,
                eX,eY), false);
        }
    }

    /**
     * This method draws the five lines from the position of the starting element
     * to the position of the ending element. If at least one of both is null, then
     * this method will draw nothing.
     */
    @Override
    public void paintImpl(Graphics2D g) {
        // undo the previous transformation.
        g.transform(AffineTransform.getTranslateInstance(-absMedianX, -absMedianY));
        if (path != null) {
            g.draw(path);
        }
    }
}