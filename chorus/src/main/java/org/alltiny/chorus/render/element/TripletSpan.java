package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;
import org.alltiny.svg.parser.SVGPathParser;

import java.awt.*;
import java.awt.geom.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.PushbackInputStream;

/**
 * This class renders a triplet bracket above the triplet notes.
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 18.05.2010 18:02:00
 */
public class TripletSpan extends Visual {

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
    public TripletSpan setStartElement(NoteRender startElement) {
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
    public TripletSpan setEndElement(NoteRender endElement) {
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
            double sX = startElement.getAbsMedianX() + startElement.getBounds2D().getMinX();
            double eX = endElement.getAbsMedianX() + endElement.getBounds2D().getMaxX();
            double width = eX - sX;
            double length = Math.max(1, 0.5 * (width - LINES_SPACE));

            double sY = startElement.getAbsMedianY() - startElement.getRelativePosY() * 0.5 * LINES_SPACE;
            double eY = endElement.getAbsMedianY() - endElement.getRelativePosY() * 0.5 * LINES_SPACE;

            // draw the left corner of bracket.
            path.append(new Rectangle2D.Double(sX - 0.5, -3 * LINES_SPACE, 1, 0.75 * LINES_SPACE), false);
            path.append(new Rectangle2D.Double(sX - 0.5, -3 * LINES_SPACE, length, 1), false);
            // draw the right corner of bracket.
            path.append(new Rectangle2D.Double(eX - 0.5, -3 * LINES_SPACE, 1, 0.75 * LINES_SPACE), false);
            path.append(new Rectangle2D.Double(eX - 0.5 - length, -3 * LINES_SPACE, length, 1), false);
            // draw the number "3"
            GeneralPath number = createTripletNumber();
            number.transform(AffineTransform.getTranslateInstance(sX + 0.5 * width, -3 * LINES_SPACE));
            path.append(number, false);
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
        g.transform(AffineTransform.getTranslateInstance(-absMedianX, 0));//-absMedianY));
        if (path != null) {
            g.fill(path);
        }
    }

    private static GeneralPath createTripletNumber() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("m -4,-5 c 0.266919,0.1758 0.400383,0.5078 0.40039,0.9961 0,0.4492 -0.179044,0.8301 -0.537109,1.1426 -0.35808,0.3125 -0.781256,0.4785 -1.269531,0.498 -0.957036,0 -1.435552,-0.4525 -1.435547,-1.3574 0,-0.1302 0.05208,-0.3841 0.15625,-0.7617 0.403641,-1.0807 1.18489,-1.8929 2.34375,-2.4366 1.158846,-0.5436 2.552074,-0.8251 4.179687,-0.8447 1.510405,0 2.788075,0.3207 3.833005,0.9619 1.04491,0.6413 1.56737,1.587 1.56739,2.8369 -0.0391,0.8139 -0.31252,1.5626 -0.82032,2.2461 -0.50782,0.6836 -1.14584,1.2142 -1.91406,1.5918 -0.76824,0.3777 -1.54949,0.5665 -2.34375,0.5664 1.07421,0.046 2.02147,0.363 2.8418,0.9522 0.8203,0.5892 1.23045,1.473 1.23047,2.6514 0,0.1562 -0.0261,0.4492 -0.0781,0.8789 -0.29949,1.6862 -1.13934,2.9817 -2.51953,3.8867 -1.38022,0.9049 -3.033864,1.3672 -4.960937,1.3867 -1.764329,0 -3.204758,-0.2848 -4.321289,-0.8545 -1.116539,-0.5696 -1.674807,-1.4176 -1.674805,-2.5439 0,-0.3711 0.02604,-0.6511 0.07813,-0.8399 0.149737,-0.5794 0.436196,-1.0709 0.859375,-1.4746 0.423174,-0.4036 0.98958,-0.6055 1.699219,-0.6055 0.462234,0 0.8317,0.1482 1.108398,0.4444 0.276687,0.2962 0.415033,0.6559 0.415039,1.0791 0,0.3125 -0.107428,0.6445 -0.322265,0.9961 -0.149746,0.2213 -0.341803,0.4166 -0.576172,0.5859 -0.23438,0.1693 -0.374354,0.2539 -0.419922,0.2539 0.117183,0.3516 0.488276,0.6576 1.113281,0.918 0.520827,0.2148 1.136061,0.3223 1.845703,0.3223 1.119783,0 2.037751,-0.306 2.753907,-0.918 0.937493,-0.8073 1.406233,-1.849 1.406253,-3.125 0,-0.7292 -0.20184,-1.3542 -0.60547,-1.875 -0.716159,-0.8529 -1.982433,-1.2793 -3.79883,-1.2793 l 0.24414,-1.5235 c 0.156242,0.052 0.341789,0.078 0.556641,0.078 1.367178,0 2.607411,-0.371 3.720699,-1.1132 0.93098,-0.612 1.39648,-1.4811 1.39649,-2.6075 0,-0.4687 -0.0912,-0.8658 -0.27344,-1.1914 -0.35808,-0.7226 -1.26629,-1.0839 -2.724608,-1.0839 -0.585948,0 -1.158864,0.068 -1.71875,0.205 -0.618498,0.1563 -1.097013,0.4753 -1.435547,0.9571 z".getBytes())));
            double pathScale = 0.6;
            path.transform(AffineTransform.getScaleInstance(pathScale, pathScale));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }
}