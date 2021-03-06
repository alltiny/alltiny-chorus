package org.alltiny.chorus.render.element;

import org.alltiny.svg.parser.SVGPathParser;
import org.alltiny.chorus.render.Visual;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class ClefF extends Visual {

    private GeneralPath path;

    public ClefF() {
        setPadding(new Rectangle2D.Double(-3, -5, 6, 10));
        try {
            path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 243.97900,540.86798 C 244.02398,543.69258 242.76360,546.43815 240.76469,548.40449 C 238.27527,550.89277 235.01791,552.47534 231.69762,553.53261 C 231.25590,553.77182 230.58970,553.45643 231.28550,553.13144 C 232.62346,552.52289 234.01319,552.00050 235.24564,551.18080 C 237.96799,549.49750 240.26523,546.84674 240.82279,543.61854 C 241.14771,541.65352 241.05724,539.60795 240.56484,537.67852 C 240.20352,536.25993 239.22033,534.79550 237.66352,534.58587 C 236.25068,534.36961 234.74885,534.85905 233.74057,535.88093 C 233.47541,536.14967 232.95916,536.89403 233.04435,537.74747 C 233.64637,537.27468 233.60528,537.32732 234.09900,537.10717 C 235.23573,536.60031 236.74349,537.32105 237.02700,538.57272 C 237.32909,539.72295 237.09551,541.18638 235.96036,541.79960 C 234.77512,542.44413 233.02612,542.17738 232.36450,540.90866 C 231.26916,538.95418 231.87147,536.28193 233.64202,534.92571 C 235.44514,533.42924 238.07609,533.37089 240.19963,534.13862 C 242.38419,534.95111 243.68629,537.21483 243.89691,539.45694 C 243.95419,539.92492 243.97896,540.39668 243.97900,540.86798 z".getBytes())));
            path.transform(AffineTransform.getTranslateInstance(-231.5, -545.7));
            path.transform(AffineTransform.getScaleInstance(1.5, 1.5));
            path.append(new Ellipse2D.Double(20, -15, 4, 4), false);
            path.append(new Ellipse2D.Double(20,  -7, 4, 4), false);
            path.transform(AffineTransform.getTranslateInstance(-11.5, 0));
        } catch (Exception ex) { throw new Error("SVG could not be parsed"); }
    }

    public void paintImpl(Graphics2D g) {
        g.fill(path);
    }

    public Rectangle2D getBounds2D() {
        return path.getBounds2D();
    }
}
