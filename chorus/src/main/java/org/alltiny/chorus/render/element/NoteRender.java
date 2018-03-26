package org.alltiny.chorus.render.element;

import org.alltiny.chorus.dom.DurationElement;
import org.alltiny.chorus.render.Visual;
import org.alltiny.chorus.dom.Note;
import org.alltiny.svg.parser.SVGPathParser;
import org.alltiny.svg.parser.SVGParseException;

import java.awt.*;
import java.awt.geom.*;
import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 05.02.2009 16:36:20
 */
public class NoteRender extends Visual {

    private static final float WIDTH = 11;
    private static final float HEIGHT = 7.5f;
    private static final float LINE_WEIGHT = 1;

    private final ArrayList<Shape> paths = new ArrayList<Shape>();
    private final Rectangle2D bounds2D = new Rectangle2D.Double();

    private boolean drawStem = true;
    private boolean drawAccidentalSign = false;
    private Note note;

    public NoteRender(Note note) {
        setPadding(new Rectangle2D.Double(-5, -2, 10, 4));
        setNote(note);
    }

    public void setNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("note can not be null");
        }
        this.note = note;
        // reconfigure this rendering element.
        update();
    }

    public void setDrawAccidentalSign(boolean draw) {
        drawAccidentalSign = draw;
        // reconfigure this rendering element.
        update();
    }

    private void update() {
        paths.clear();

        // determine on which line the note must be placed.
        final int line = getRelativePosY();

        // create the head of the note.
        GeneralPath head;
        final double length = (double)note.getDuration() / note.getDivision();
        if (length >= 2) {
            head = createHead4DoubleNote();
        } else if (length >= 1) {
            head = createHead4FullNote();
        } else if (length >= 0.5) {
            head = createHead4HalfNote();
        } else {
            head = createHead4QuarterNote();
        }
        head.transform(AffineTransform.getTranslateInstance(0, getRelativePosY() * -0.5 * LINES_SPACE));
        paths.add(head);

        // create additional lines above if necessary.
        int supported = 5; // the highest supported line is 5
        while (line > supported) {
            supported++;
            paths.add(new Rectangle2D.Float((float)head.getBounds2D().getMinX() - 1, supported * -0.5f * LINES_SPACE - 0.5f * LINE_WEIGHT, (float)head.getBounds2D().getWidth() + 2, LINE_WEIGHT));
            supported++;
        }

        // create additional lines below if necessary.
        supported = -5; // the highest supported line is 5
        while (line < supported) {
            supported--;
            paths.add(new Rectangle2D.Float((float)head.getBounds2D().getMinX() - 1, supported * -0.5f * LINES_SPACE - 0.5f * LINE_WEIGHT, (float)head.getBounds2D().getWidth() + 2, LINE_WEIGHT));
            supported--;
        }

        // draw accidental sign if necessary
        if (drawAccidentalSign) {
            GeneralPath sign = null;
            switch (note.getSign()) {
                case SHARP: sign = Sharp.createSharp(); break;
                case FLAT : sign = Flat.createFlat(); break;
                case NONE : sign = Natural.createNatural(); break;
                case DFLAT: sign = DFlat.createDFlat(); break;
                case DSHARP: sign = DSharp.createDSharp(); break;
            }

            if (sign != null) { // place it correctly.
                double dx = sign.getBounds2D().getMaxX() - head.getBounds2D().getMinX() + 1;
                sign.transform(AffineTransform.getTranslateInstance(-dx, getRelativePosY() * -0.5 * LINES_SPACE));
                paths.add(sign);
            }
        }

        if (drawStem && length < 1) {
            final boolean stemDownwards = line >= 0;

            GeneralPath stem = createStem();
            if (stemDownwards) {
                stem.transform(AffineTransform.getScaleInstance(-1, -1));
            }
            stem.transform(AffineTransform.getTranslateInstance(0, getRelativePosY() * -0.5 * LINES_SPACE));
            paths.add(stem);

            final int numberOfFlags = calcNumberOfFlags(note);
            for (int index = 0; index < numberOfFlags; index++) {
                GeneralPath flag = createFlagPath();
                // shift the flag by the half width of the stem-stroke
                flag.transform(AffineTransform.getTranslateInstance(0.4, 0));
                // make the flags progressively smaller.
                flag.transform(AffineTransform.getScaleInstance(Math.pow(0.9, index), Math.pow(0.9, index)));
                if (stemDownwards) {
                    flag.transform(AffineTransform.getScaleInstance(-1, 1));
                }
                // move the flag to the top of the stem
                flag.transform(AffineTransform.getTranslateInstance(0.5f * WIDTH - 0.35f - 0.48f, (-3.25 + 0.65 * index) * LINES_SPACE));
                if (stemDownwards) {
                    flag.transform(AffineTransform.getScaleInstance(-1, -1));
                }
                flag.transform(AffineTransform.getTranslateInstance(0, getRelativePosY() * -0.5 * LINES_SPACE));
                paths.add(flag);
            }
        }

        // append single or double dots if necessary.
        final float dotY = (line < 5 && line > -5) ? ((float)Math.floor((float)line / 2) * -LINES_SPACE - 0.5f * LINES_SPACE) : (-0.5f * line * LINES_SPACE);
        if (note.getDuration() == 3) { // this note is single dotted.
            paths.add(new Ellipse2D.Float((float)head.getBounds2D().getMaxX() + 2, dotY - 0.5f * DOT_SIZE, DOT_SIZE, DOT_SIZE));
        }
        if (note.getDuration() == 7) { // this note is double dotted.
            paths.add(new Ellipse2D.Float((float)head.getBounds2D().getMaxX() + 2, dotY - 0.5f * DOT_SIZE, DOT_SIZE, DOT_SIZE));
            paths.add(new Ellipse2D.Float((float)head.getBounds2D().getMaxX() + 5, dotY - 0.5f * DOT_SIZE, DOT_SIZE, DOT_SIZE));
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

    public void paintImpl(Graphics2D graphics) {
        for (Shape shape : paths) {
            graphics.fill(shape);
        }
    }

    public int getRelativePosY() {
        int height;
        switch (note.getNote()) {
            case B: height = 0; break;
            case A: height = -1; break;
            case G: height = -2; break;
            case F: height = -3; break;
            case E: height = -4; break;
            case D: height = -5; break;
            case C: height = -6; break;
            default: return 0; // should not happen.
        }

        // if the clef is G then the height must not be shifted.
        switch (note.getSequence().getClef()) {
            case G8basso: height += 7; break;
            case F: height += 12; break;
            default: break;
        }

        height += (note.getOctave() - 4) * 7;

        return height;
    }

    private static GeneralPath createFlagPath() {
        try {
            GeneralPath flag = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 350.88004,566.13535 C 350.82493,573.71278 353.37325,577.10146 360.44529,580.65196 C 367.40151,584.14431 377.49073,582.28887 365.80922,632.41783 C 372.80333,591.07071 369.31484,593.80133 350.94053,585.99454 L 350.88004,566.13535 z".getBytes())));
            flag.transform(AffineTransform.getTranslateInstance(-350.808, -566));//-487.405));
            flag.transform(AffineTransform.getScaleInstance(0.45, 0.45));
            return flag;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static GeneralPath createHead4DoubleNote() {
        GeneralPath head = new GeneralPath();
        head.append(new Rectangle2D.Float(-0.8f * LINES_SPACE, -0.5f * LINES_SPACE, 1.6f * LINES_SPACE, 0.3f * LINES_SPACE), false);
        head.append(new Rectangle2D.Float(-0.8f * LINES_SPACE,  0.2f * LINES_SPACE, 1.6f * LINES_SPACE, 0.3f * LINES_SPACE), false);
        head.append(new Rectangle2D.Float(-0.9f * LINES_SPACE, -0.7f * LINES_SPACE, 0.1f * LINES_SPACE, 1.4f * LINES_SPACE), false);
        head.append(new Rectangle2D.Float(+0.8f * LINES_SPACE, -0.7f * LINES_SPACE, 0.1f * LINES_SPACE, 1.4f * LINES_SPACE), false);
        return head;
    }

    private static GeneralPath createHead4FullNote() {
        GeneralPath head = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        head.append(new Ellipse2D.Float(-0.4f * WIDTH, -0.3f * HEIGHT, 0.8f * WIDTH, 0.6f * HEIGHT), false);
        head.transform(AffineTransform.getScaleInstance(-1, 1));
        head.transform(AffineTransform.getRotateInstance(0.6));
        head.append(new Ellipse2D.Float(-0.5f * WIDTH / HEIGHT * LINES_SPACE, -0.5f * LINES_SPACE, WIDTH / HEIGHT * LINES_SPACE, LINES_SPACE), false);
        return head;
    }

    private static GeneralPath createHead4QuarterNote() {
        GeneralPath head = new GeneralPath();
        head.append(new Ellipse2D.Float(-0.5f * WIDTH, -0.5f * HEIGHT, WIDTH, HEIGHT), true);
        head.transform(AffineTransform.getRotateInstance(-0.5));
        return head;
    }

    private static GeneralPath createHead4HalfNote() {
        GeneralPath head = new GeneralPath();
        head.append(new Ellipse2D.Float(-0.4f * WIDTH, -0.225f * HEIGHT, 0.8f * WIDTH, 0.45f * HEIGHT), true);
        head.transform(AffineTransform.getScaleInstance(-1, 1));
        head.append(new Ellipse2D.Float(-0.5f * WIDTH, -0.5f * HEIGHT, WIDTH, HEIGHT), true);
        head.transform(AffineTransform.getRotateInstance(-0.5));
        return head;
    }

    private static GeneralPath createStem() {
        GeneralPath shaft = new GeneralPath();
        final float w2 = 0.5f * WIDTH;
        //final float h2 = 0.666f*HEIGHT;
        //shaft.moveTo(-w2, 0);
        //shaft.transform(AffineTransform.getRotateInstance(-0.5));
        shaft.moveTo(w2 - 0.35f, -1.4f);
        shaft.lineTo((float)shaft.getCurrentPoint().getX() - 1, (float)shaft.getCurrentPoint().getY());
        shaft.lineTo((float)shaft.getCurrentPoint().getX(), -3.25f * LINES_SPACE);
        shaft.lineTo((float)shaft.getCurrentPoint().getX() + 1, (float)shaft.getCurrentPoint().getY());
        shaft.closePath();
        return shaft;
    }

    public static int calcNumberOfFlags(DurationElement note) {
        final int flags = (int)Math.round(Math.log(note.getDivision()) / Math.log(2)) -
            (int)Math.floor(Math.log(note.getDuration()) / Math.log(2)) - 2;
        return Math.max(flags, 0);
    }
}
