package org.alltiny.chorus.render.element;

import org.alltiny.chorus.render.Visual;
import org.alltiny.chorus.dom.Rest;
import org.alltiny.svg.parser.SVGPathParser;

import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.*;
import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class RestRender extends Visual {

    private final ArrayList<Shape> paths = new ArrayList<Shape>();
    private final Rectangle2D bounds2D = new Rectangle2D.Double();

    private Rest rest;

    public RestRender(Rest rest) {
        setPadding(new Rectangle2D.Float(-5,-5,10,10));
        setRest(rest);
    }

    public void setRest(Rest rest) {
        this.rest = rest;
        update();
    }

    protected void update() {
        paths.clear();
        Shape path;
        final double length = rest.getLength();
        if (length >= 2) {
            path = createRestDouble();
        } else if (length >= 1) {
            path = createRestFull();
        } else if (length >= 0.5) {
            path = createRestHalf();
        } else if (length >= 0.25) {
            path = createRest4();
        } else if (length >= 0.125) {
            path = createRest8();
        } else {
            path = createRest16();
        }
        paths.add(path);

        // append single or double dots if necessary.
        if (rest.getDuration() == 3) { // this note is single dotted.
            paths.add(new Ellipse2D.Float((float)path.getBounds2D().getMaxX() + 2, -0.5f * LINES_SPACE - 0.5f * DOT_SIZE, DOT_SIZE, DOT_SIZE));
        }
        if (rest.getDuration() == 7) { // this note is double dotted.
            paths.add(new Ellipse2D.Float((float)path.getBounds2D().getMaxX() + 2, -0.5f * LINES_SPACE - 0.5f * DOT_SIZE, DOT_SIZE, DOT_SIZE));
            paths.add(new Ellipse2D.Float((float)path.getBounds2D().getMaxX() + 5, -0.5f * LINES_SPACE - 0.5f * DOT_SIZE, DOT_SIZE, DOT_SIZE));
        }

        // precompute the bounds of this compound shape.
        bounds2D.setRect(0, 0, 0, 0);
        for (Shape shape : paths) {
            bounds2D.add(shape.getBounds2D());
        }
    }

    public void paintImpl(Graphics2D g) {
        for (Shape shape : paths) {
            g.fill(shape);
        }
    }

    public Rectangle2D getBounds2D() {
        return bounds2D;
    }

    public static Shape createRestDouble() {
        return new Rectangle2D.Float(-3, -LINES_SPACE, 6, LINES_SPACE);
    }

    public static Shape createRestFull() {
        return new Rectangle2D.Float(-6, -LINES_SPACE, 12, 0.5f * LINES_SPACE);
    }

    public static Shape createRestHalf() {
        return new Rectangle2D.Float(-4.5f, -0.5f * LINES_SPACE, 9, 0.5f * LINES_SPACE);
    }

    public static Shape createRest4() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M -1.9826943,13.997956 L -0.41759439,15.908431 C 1.1254557,18.000794 0.012263608,20.311906 -1.5071703,21.482394 C -4.5098403,23.925435 -4.6166883,24.565826 -3.5828583,25.795232 L 0.99090566,31.175356 C -0.76548039,30.372698 -3.6744243,29.671848 -4.4084103,30.444331 C -5.7394203,31.910073 -3.0639723,36.946572 -1.4998263,38.768219 C -1.1907123,39.130445 -0.84169239,38.75562 -1.0476843,38.507287 C -2.2423623,36.898456 -2.3225703,33.8717 -1.3721883,33.063299 C -0.46282839,32.259822 2.2827477,32.871595 3.4428837,34.122401 C 3.8412597,34.492776 4.6138737,33.851634 4.2105117,33.358005 L 1.3149057,29.888356 C -0.41451639,27.920618 0.29889561,25.36051 1.9802217,24.002131 C 3.4938057,22.744413 5.5657137,21.325173 4.4361057,19.918156 L -1.2302943,13.245556 C -1.8708063,13.119009 -2.1254883,13.632 -1.9826943,13.997956 z".getBytes())));
            path.transform(AffineTransform.getTranslateInstance(0,-27));
            return path;
        } catch (Exception ex) {
            throw new Error("SVG could not be parsed");
        }
    }

    public static Shape createRest8() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M -2.4551544,10.111018 C -3.8569944,10.412351 -4.5719904,11.602345 -4.5272784,12.578818 C -4.3158324,15.587873 -1.3917684,16.11494 2.3112456,14.659618 L -1.8863544,26.352418 C -1.2771624,26.893563 0.036063633,26.667999 0.16024564,26.248018 L 4.5324456,11.291818 C 4.3529856,10.963495 3.7889736,10.909883 3.7098456,11.074018 C 3.0446736,12.484698 1.8395556,13.906977 0.91264564,13.478818 C 0.58864564,13.302418 0.48244564,13.120618 0.26464564,12.150418 C 0.054045632,11.187418 -0.19795436,10.750018 -0.73975436,10.391818 C -1.2401544,10.069618 -1.8863544,9.9634174 -2.4551544,10.111018 z".getBytes())));
            path.transform(AffineTransform.getTranslateInstance(0,-17.5));
            return path;
        } catch (Exception ex) {
            throw new Error("SVG could not be parsed");
        }
    }

    public static Shape createRest16() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M -2.4551544,10.111018 C -3.8569944,10.412351 -4.5719904,11.602345 -4.5272784,12.578818 C -4.3158324,15.587873 -1.3917684,16.11494 2.3112456,14.659618 L -1.8863544,26.352418 C -1.2771624,26.893563 0.036063633,26.667999 0.16024564,26.248018 L 4.5324456,11.291818 C 4.3529856,10.963495 3.7889736,10.909883 3.7098456,11.074018 C 3.0446736,12.484698 1.8395556,13.906977 0.91264564,13.478818 C 0.58864564,13.302418 0.48244564,13.120618 0.26464564,12.150418 C 0.054045632,11.187418 -0.19795436,10.750018 -0.73975436,10.391818 C -1.2401544,10.069618 -1.8863544,9.9634174 -2.4551544,10.111018 z".getBytes())));
            path.transform(AffineTransform.getTranslateInstance(0,-17.5));
            path.append(new Rectangle2D.Float(-0.5f, -LINES_SPACE, 1, 2 * LINES_SPACE), false);
            return path;
        } catch (Exception ex) {
            throw new Error("SVG could not be parsed");
        }
    }
}
