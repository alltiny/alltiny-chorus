package org.alltiny.chorus.render.element;

import org.alltiny.chorus.base.type.AccidentalSign;
import org.alltiny.chorus.render.Visual;
import org.alltiny.chorus.dom.Sequence;
import org.alltiny.chorus.base.type.Clef;

import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 28.01.2010 21:50:36
 */
public class KeyRender extends Visual {

    private final ArrayList<Shape> paths = new ArrayList<Shape>();
    private final Rectangle2D bounds2D = new Rectangle2D.Double();

    private Sequence sequence;

    public KeyRender(Sequence note) {
        setPadding(new Rectangle2D.Double(-5, -5, 10, 10));
        setSequence(note);
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
        // reconfigure this rendering element.
        update();
    }

    private void update() {
        paths.clear();

        // determine the offset for the current clef.
        int offset = 0; // for G and G8basso
        if (sequence.getClef() == Clef.F) {
            offset = 2;
        }

        double currentX = 0;
        //
        if (sequence.getKey().getFMod() == AccidentalSign.SHARP) {
            GeneralPath sharp = Sharp.createSharp();
            sharp.transform(AffineTransform.getTranslateInstance(currentX, (offset - 4) * 0.5 * LINES_SPACE));
            currentX += sharp.getBounds2D().getWidth() + 1;
            paths.add(sharp);
        }
        if (sequence.getKey().getCMod() == AccidentalSign.SHARP) {
            GeneralPath sharp = Sharp.createSharp();
            sharp.transform(AffineTransform.getTranslateInstance(currentX, (offset - 1) * 0.5 * LINES_SPACE));
            currentX += sharp.getBounds2D().getWidth() + 1;
            paths.add(sharp);
        }
        if (sequence.getKey().getGMod() == AccidentalSign.SHARP) {
            GeneralPath sharp = Sharp.createSharp();
            sharp.transform(AffineTransform.getTranslateInstance(currentX, (offset - 5) * 0.5 * LINES_SPACE));
            currentX += sharp.getBounds2D().getWidth() + 1;
            paths.add(sharp);
        }
        if (sequence.getKey().getDMod() == AccidentalSign.SHARP) {
            GeneralPath sharp = Sharp.createSharp();
            sharp.transform(AffineTransform.getTranslateInstance(currentX, (offset - 2) * 0.5 * LINES_SPACE));
            currentX += sharp.getBounds2D().getWidth() + 1;
            paths.add(sharp);
        }
        if (sequence.getKey().getAMod() == AccidentalSign.SHARP) {
            GeneralPath sharp = Sharp.createSharp();
            sharp.transform(AffineTransform.getTranslateInstance(currentX, (offset + 1) * 0.5 * LINES_SPACE));
            currentX += sharp.getBounds2D().getWidth() + 1;
            paths.add(sharp);
        }
        if (sequence.getKey().getEMod() == AccidentalSign.SHARP) {
            GeneralPath sharp = Sharp.createSharp();
            sharp.transform(AffineTransform.getTranslateInstance(currentX, (offset - 3) * 0.5 * LINES_SPACE));
            currentX += sharp.getBounds2D().getWidth() + 1;
            paths.add(sharp);
        }
        if (sequence.getKey().getBMod() == AccidentalSign.FLAT) {
            GeneralPath flat = Flat.createFlat();
            flat.transform(AffineTransform.getTranslateInstance(currentX, offset * 0.5 * LINES_SPACE));
            currentX += flat.getBounds2D().getWidth() + 1;
            paths.add(flat);
        }
        if (sequence.getKey().getEMod() == AccidentalSign.FLAT) {
            GeneralPath flat = Flat.createFlat();
            flat.transform(AffineTransform.getTranslateInstance(currentX, (offset - 3) * 0.5 * LINES_SPACE));
            currentX += flat.getBounds2D().getWidth() + 1;
            paths.add(flat);
        }
        if (sequence.getKey().getAMod() == AccidentalSign.FLAT) {
            GeneralPath flat = Flat.createFlat();
            flat.transform(AffineTransform.getTranslateInstance(currentX, (offset + 1) * 0.5 * LINES_SPACE));
            currentX += flat.getBounds2D().getWidth() + 1;
            paths.add(flat);
        }
        if (sequence.getKey().getDMod() == AccidentalSign.FLAT) {
            GeneralPath flat = Flat.createFlat();
            flat.transform(AffineTransform.getTranslateInstance(currentX, (offset - 2) * 0.5 * LINES_SPACE));
            currentX += flat.getBounds2D().getWidth() + 1;
            paths.add(flat);
        }
        if (sequence.getKey().getGMod() == AccidentalSign.FLAT) {
            GeneralPath flat = Flat.createFlat();
            flat.transform(AffineTransform.getTranslateInstance(currentX, (offset + 2) * 0.5 * LINES_SPACE));
            currentX += flat.getBounds2D().getWidth() + 1;
            paths.add(flat);
        }
        if (sequence.getKey().getCMod() == AccidentalSign.FLAT) {
            GeneralPath flat = Flat.createFlat();
            flat.transform(AffineTransform.getTranslateInstance(currentX, (offset - 1) * 0.5 * LINES_SPACE));
            currentX += flat.getBounds2D().getWidth() + 1;
            paths.add(flat);
        }

        // precompute the bounds of this compound shape.
        if (paths.size() > 0) {
            bounds2D.setRect(paths.get(0).getBounds2D());
        } else {
            bounds2D.setRect(0, 0, 0, 0);
        }
        for (Shape shape : paths) {
            bounds2D.add(shape.getBounds2D());
        }
    }

    public Rectangle2D getBounds2D() {
        return bounds2D;
    }

    public void paintImpl(Graphics2D g) {
        for (Shape shape : paths) {
            g.fill(shape);
        }
    }
}
