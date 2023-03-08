package org.alltiny.chorus.render.element;

import org.alltiny.svg.parser.SVGPathParser;
import org.alltiny.chorus.render.Visual;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class ClefG extends Visual {

    private GeneralPath path;

    public ClefG() {
        setPadding(new Rectangle2D.Double(-3, -5, 6, 10));
        try {
            // load the path an set its scaling and offset parameters.
            path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 20.52091,28.02079 C 20.99456,31.08719 23.86458,49.97416 24.07165,51.99297 C 24.27872,54.01178 24.05248,56.37502 22.23951,57.55497 C 20.6424,58.54915 18.15619,58.75735 16.8094,57.23696 C 15.35456,55.69274 16.28302,52.59874 18.54502,52.55196 C 20.30058,52.27158 21.5382,54.44657 20.70042,55.91077 C 20.44,56.43919 19.51021,57.21838 18.44035,57.47569 C 18.80473,57.6762 18.31903,57.41163 18.9184,57.72696 C 20.46335,58.53977 22.55016,57.13364 23.2509,55.53506 C 23.72503,53.83117 23.72624,52.71771 23.39706,50.42872 C 23.06954,48.15122 20.46473,31.64564 19.93464,28.42212 C 19.54214,25.70399 19.2772,23.65834 20.76611,20.98784 C 21.66851,19.7273 22.86757,17.69957 24.56771,17.57134 C 25.97021,19.38356 26.30521,21.82297 26.24646,24.05491 C 26.11198,27.56209 23.58395,30.14111 22.33366,31.38315 C 20.52217,33.30215 18.37734,35.12514 16.7118,37.61088 C 15.36282,39.48536 15.14212,41.96246 15.89113,44.11562 C 17.03496,47.67422 19.64171,48.2824 22.29593,48.16845 C 25.84908,47.80587 27.3926,44.70219 26.2393,41.18872 C 25.30906,38.07359 19.39021,38.10922 18.97537,42.28084 C 18.87824,44.11525 19.73792,44.74251 20.8399,45.53496 C 19.81696,45.959 18.60684,44.66522 18.0744,43.90096 C 16.28479,41.1752 18.29876,37.23208 21.28939,36.44994 C 23.37101,35.95614 24.71528,36.31167 26.1134,37.24297 C 29.87368,40.05367 29.19286,46.29704 24.8209,48.15762 C 22.61438,49.11388 21.21297,48.92194 19.33428,48.62377 C 16.05349,47.83082 13.64742,44.73044 13.46406,41.39645 C 13.06023,37.94835 15.1126,34.8268 17.4687,32.5181 C 18.78398,31.21237 19.98926,30.10959 21.38475,28.80243 C 23.44258,26.75724 25.76676,24.2272 25.4624,21.10094 C 25.26235,19.88587 25.00763,19.83947 24.69872,19.26276 C 21.87819,21.4944 20.20727,25.67133 20.52091,28.02079 z".getBytes())));
            double pathScale = 1.5;
            double pathOffsetX = -21.8;
            double pathOffsetY = -36.5;

            path.transform(AffineTransform.getTranslateInstance(pathOffsetX, pathOffsetY));
            path.transform(AffineTransform.getScaleInstance(pathScale, pathScale));
        } catch (Exception ex) {
            throw new Error("SVG could not be parsed");
        }
    }

    public void paintImpl(Graphics2D g) {
        g.fill(path);
    }

    public Rectangle2D getBounds2D() {
        return path.getBounds2D();
    }
}
