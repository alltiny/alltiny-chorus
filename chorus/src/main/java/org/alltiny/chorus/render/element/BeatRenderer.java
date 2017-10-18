package org.alltiny.chorus.render.element;

import org.alltiny.chorus.dom.Bar;
import org.alltiny.chorus.render.Visual;
import org.alltiny.svg.parser.SVGPathParser;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.PushbackInputStream;

/**
 * This class represents
 *
 * @author <a href="mailto:ralf.hergert.de@gmail.com">Ralf Hergert</a>
 * @version 03.12.2008 16:58:50
 */
public class BeatRenderer extends Visual {

    public static final double SPACING = 0.2;

    private GeneralPath path = null;

    public BeatRenderer(final Bar bar) {
        setPadding(new Rectangle2D.Float(-10,-2,30,4));

        path = new GeneralPath();
        {
            GeneralPath durationPath = createPathForNumber(bar.getDuration());
            // move the duration path onto the center line and one line higher.
            durationPath.transform(AffineTransform.getTranslateInstance(-0.5 * (durationPath.getBounds().getMinX() + durationPath.getBounds().getMaxX()), -LINES_SPACE));
            path.append(durationPath, false);
        }
        {
            GeneralPath divisionPath = createPathForNumber(bar.getDivision());
            // move the division path onto the center line and one line lower.
            divisionPath.transform(AffineTransform.getTranslateInstance(-0.5 * (divisionPath.getBounds().getMinX() + divisionPath.getBounds().getMaxX()), LINES_SPACE));
            path.append(divisionPath, false);
        }
    }

    public void paintImpl(Graphics2D g) {
        g.fill(path);
    }

    public Rectangle2D getBounds2D() {
        return path.getBounds2D();
    }

    public static GeneralPath createPathForNumber(int number) {
        int digit = number % 10;
        GeneralPath path = createPathForDigit(digit);

        if (number > 9) {
            GeneralPath leading = createPathForNumber(number / 10);
            leading.transform(AffineTransform.getTranslateInstance(path.getBounds().getMinX() - leading.getBounds().getMaxX() - SPACING, 0));
            path.append(leading, false);
        }
        return path;
    }

    public static GeneralPath createPathForDigit(int digit) {
        switch (digit) {
            case 0: return createPath0();
            case 1: return createPath1();
            case 2: return createPath2();
            case 3: return createPath3();
            case 4: return createPath4();
            case 5: return createPath5();
            case 6: return createPath6();
            case 7: return createPath7();
            case 8: return createPath8();
            case 9: return createPath9();
            default: throw new IllegalArgumentException("digit is out of bounds");
        }
    }

    public static GeneralPath createPath0() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 14.793298,63.901889 C 10.034399,49.837715 11.162201,32.97883 20.676001,21.106693 26.916052,13.438644 37.279609,9.6381448 47.014477,11.140526 c 11.21758,1.582177 21.056512,10.157777 24.0323,21.11435 3.950414,12.435399 3.616275,26.741872 -3.299015,38.094675 C 63.308242,77.861725 55.483588,83.665285 46.581145,84.144376 40.45999,84.690832 33.92658,84.535766 28.508292,81.264533 21.876372,77.541051 17.428164,70.882166 14.793298,63.901889 z M 53.008414,66.63387 C 55.267366,54.518689 55.168026,41.991988 53.220739,29.844383 52.271755,24.931921 50.34725,19.186897 45.363932,17.086967 c -4.25899,-1.446575 -8.997893,1.316253 -10.475327,5.431828 -3.717803,7.937709 -3.715493,16.912316 -3.986874,25.492336 0.0498,8.901165 0.47788,18.250791 4.671472,26.299623 1.781887,3.603395 6.386203,5.848827 10.141984,3.92452 4.271171,-2.250462 6.34736,-7.105549 7.293227,-11.601404 z".getBytes())));
            path.transform(AffineTransform.getTranslateInstance(-14.793, -47));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath1() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,34 c 0,-1.87416 0.189476,-1.95477 6.398241,-2.7223 2.178947,-0.26937 3.602813,-0.821912 4.544414,-1.76351 l 1.371234,-1.37123 0,-32.639479 0,-17.03154 -6.156948,0 -6.156941,0 0,-2.26171 0,-2.26171 4.631656,-0.31746 c 5.810899,-0.39829 10.659093,-2.46806 14.176713,-6.05225 2.53264,-2.58057 2.56591,-2.5935 6.67446,-2.59352 l 4.12913,-10e-6 -0.20997,21.99911 c -0.11548,12.09951 -0.0352,38.538659 0.17841,39.677309 0.46974,2.50393 2.85489,4.15816 6.75578,4.68549 4.3832,0.59253 4.7101,0.77616 4.7101,2.64573 l 0,3.71961 -20.52314,0 -20.523139,0 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath2() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,26 l 1.41221,-0.751799 c 6.33514,-3.37255 2.88162,-10.487907 20.23136,-24.18416 8.5431,-6.744102 11.96207,-11.531961 11.89397,-19.16527 -0.0662,-7.41988 -2.62392,-10.27305 -8.12943,-12.09004 -2.05008,-0.67658 -5.96285,-0.641065 -9.83259,0.288215 -0.23082,3.06133 5.77922,14.381037 -4.06005,17.392885 -6.03347,1.62465 -10.91717,-1.91642 -10.92523,-7.92164 -0.0136,-10.13179 15.2153,-15.95671 30.24048,-14.629021 14.52037,1.283082 20.16835,7.411811 20.17493,16.043441 0.007,9.5669 -5.25169,16.562387 -17.98007,22.2307 -14.8582,6.616781 -22.8809,15.60459 -23.05957,20.912262 l 43.98052,0.07118 0,13.48663 -53.94653,0 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath3() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,37 c -6.1021,-1.09949 -12.55572,-3.375491 -16.57342,-8.34169 -3.26751,-4.085382 -2.36405,-10.945673 2.39237,-13.486865 4.77675,-2.74884 12.31233,0.239882 12.81098,6.008917 0.27692,3.663537 -1.67046,7.075357 -1.5528,10.701618 2.94812,1.339761 6.40548,0.86396 9.54032,0.489307 5.95604,-0.838081 10.52314,-6.066804 11.26335,-11.924336 0.57889,-4.544629 0.78543,-9.514401 -1.36203,-13.696771 -1.58983,-3.07466 -4.8512,-4.763339 -8.24829,-4.727772 -2.82595,-0.211097 -8.48742,-0.481698 -8.48742,-0.481698 l 0,-5.86376 c 0,0 7.71659,0.01472 11.14696,-1.718955 4.87791,-3.480325 5.18972,-10.062244 4.91437,-15.51615 -0.24538,-3.51412 -1.90144,-7.425158 -5.19702,-9.037503 -3.50062,-1.029507 -7.32133,-0.852929 -10.88542,-0.288632 -0.13838,4.443298 2.69645,9.04451 0.60376,13.393744 -2.84035,5.531191 -12.09307,6.058251 -14.99133,0.291966 -2.64308,-4.416578 -0.10138,-9.973054 3.66666,-12.834115 7.50529,-5.808525 17.62541,-6.967809 26.79472,-5.8078 6.80626,0.979058 14.37894,3.917633 17.42341,10.572262 1.47034,3.270274 1.82263,7.018504 0.38832,10.369376 -2.10308,6.322215 -8.42514,9.939975 -14.50387,11.638317 -1.67348,0.296724 -2.74693,1.417135 -0.39309,1.562211 6.228,0.909776 12.87042,3.20303 16.41159,8.776811 2.97212,4.507378 2.74515,10.525254 0.93156,15.429073 -2.67335,5.364023 -7.69135,9.4004 -13.14855,11.705702 -7.25817,2.612015 -15.27057,3.721667 -22.94513,2.786743 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath4() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,37 l 0.0843,-19.306007 -32.83701,0 0.0517,-9.08881 36.24197,-45.423629 13.54822,-0.02046 0,43.97815 10.55475,0 0,10.55475 -10.55475,0 0,19.35039 z m 0.058,-61.101298 l -24.84591,31.200644 24.87227,0.0399 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath5() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,36 c -6.90872,-1.034628 -14.39669,-4.261864 -17.62434,-10.850479 -2.38972,-4.59667 0.67618,-10.790113 5.78017,-11.675113 4.84632,-1.285017 10.75497,2.245411 10.75098,7.536274 0.14759,3.212748 -1.57008,6.162593 -1.59062,9.333357 1.85537,1.80804 5.16454,1.521961 7.63696,1.1821 4.94743,-1.184859 8.84664,-5.32271 10.78685,-9.893706 1.62392,-4.766271 1.35694,-9.929878 1.08,-14.884781 -1.12337,-5.315263 -5.38821,-10.429029 -11.04796,-11.001767 -4.74333,-0.626144 -9.7422,0.810912 -13.11748,4.283765 -1.51158,2.618468 -6.68256,2.274644 -5.91456,-1.375776 l 4.60512,-34.156082 42.37147,-0.04247 0,13.50126 -38.72396,0.14659 -2.18773,16.467951 c 6.97454,-3.751249 15.08312,-5.849842 22.9863,-4.342234 8.38856,1.275148 16.98592,6.592254 19.23275,15.214138 2.26684,8.612986 -0.52801,18.680205 -7.92011,23.987629 -7.6101,5.790057 -17.72483,7.861551 -27.10384,6.569344 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath6() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,36 c -10.1947,-2.443023 -17.70905,-11.682147 -19.32102,-21.856491 -3.22924,-16.701745 4.863,-35.080787 19.91775,-43.290498 7.68555,-4.486289 16.33325,-7.145578 25.14493,-8.217571 1.11316,2.907319 1.67632,6.04831 -2.64119,5.59499 -10.26604,2.04705 -19.56112,9.708815 -22.31447,19.999657 -1.10677,2.86192 -3.74247,9.965734 1.22138,5.621553 6.29209,-4.324734 14.66046,-4.619877 21.81788,-2.674765 7.83503,3.428185 13.70133,11.808554 12.74256,20.536945 0.296,5.93473 -1.35361,12.121728 -5.88818,16.214839 -6.03259,6.956707 -15.55891,9.691385 -24.52298,8.878597 -2.06364,-0.142286 -4.14151,-0.320499 -6.15666,-0.807256 z m 11.38355,-4.02287 c 6.07989,-3.610195 6.91849,-11.513066 6.82319,-17.950408 0.0582,-6.23129 -0.82143,-14.19226 -7.32972,-16.97921 -4.52857,-1.613367 -10.49765,0.638343 -12.46394,5.078979 -0.39309,7.313854 -0.0707,14.829921 1.7729,21.954741 1.07151,4.330403 4.72876,9.388322 9.72256,8.448135 l 0.67682,-0.232494 0.79819,-0.319743 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath7() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,36 l 33.4742,-58.692361 -33.3998,0.34501 -1.53206,1.44019 c -0.92121,0.86596 -2.01452,3.02857 -2.7421,5.42397 -1.13231,3.72789 -1.31936,3.98379 -2.91205,3.98379 l -1.70201,0 0,-24.62776 52.88232,0 -0.0276,6.073514 -36.66446,66.050646 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath8() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,37 c -7.21676,-1.339289 -14.56958,-5.066732 -18.36006,-11.58924 -1.8145,-4.210343 -1.82796,-9.304348 -0.002,-13.51299 2.82187,-5.647036 8.96543,-8.23799 14.72708,-9.81911 1.73266,-0.262057 -1.01101,-1.087703 -1.48758,-1.627912 -4.91439,-2.873931 -9.55903,-7.067597 -11.23447,-12.66885 -0.43688,-3.498567 -0.70289,-7.205676 0.24611,-10.638459 3.46478,-8.016384 12.09607,-12.334071 20.36163,-13.552037 9.40082,-1.510579 20.01922,-0.01648 27.28511,6.568795 4.50128,4.017488 5.60705,11.129271 3.027,16.47375 -2.93943,4.701467 -7.99288,7.505859 -13.14515,9.166153 -0.76651,0.416658 1.76569,0.962929 2.32889,1.431834 6.5372,2.794884 13.22006,7.558265 14.62098,14.99545 0.91953,5.505671 -0.008,11.763981 -4.20371,15.791879 -5.46791,5.716899 -13.27553,9.145587 -21.20716,9.134058 -4.30896,0.168692 -8.66577,0.365575 -12.95667,-0.153321 z m 16.43764,-5.53987 c 6.45331,-3.687356 7.84493,-14.243004 1.64194,-18.837316 -5.05443,-3.667277 -10.90098,-6.070406 -16.5903,-8.564544 -2.63579,1.635105 -3.79817,5.812973 -4.04928,8.97616 -0.54746,3.87209 -0.46697,7.997434 0.49279,11.854845 1.70509,4.454007 5.81006,8.259364 10.79484,8.194938 2.64407,0.102283 5.36181,-0.369113 7.71001,-1.624083 z m 1.82811,-40.52528 c 0.89556,-5.315992 1.11576,-10.829121 0.38576,-16.174107 -0.98078,-4.065811 -4.68715,-7.479793 -9.0362,-7.132123 -5.81893,-0.238098 -10.41221,5.502887 -9.92373,11.09917 0.13784,4.277556 2.97983,8.062059 6.75541,9.917655 3.36278,1.929155 6.68157,4.076316 10.35282,5.356625 0.93023,-0.671979 1.10698,-2.050603 1.46594,-3.06722 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPath9() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,32 c 14.53631,-4.730376 26.42993,-12.181286 27.92086,-27.626767 -5.57204,3.767876 -12.73465,4.606712 -17.67706,4.640838 -6.26787,-0.256267 -13.17044,-3.404179 -16.68228,-8.792831 -2.90074,-4.61616 -4.21498,-11.59309 -3.2187,-17.08723 2.21097,-12.19281 13.54148,-20.4194 28.13167,-20.4252 13.57734,-0.005 23.94464,7.72355 27.29378,20.34784 1.31471,4.95582 1.18588,15.15729 -0.26241,20.77873 -3.91767,14.608289 -17.28963,28.751257 -44.27997,33.465478 z m 23.33091,-29.65749 c 1.70374,-1.092456 3.8933,-1.864847 5.15352,-4.406821 0.83218,-7.910131 0.92637,-21.430826 -4.90777,-28.130089 -3.96578,-4.30589 -9.72779,-3.01259 -12.70986,2.85274 -3.41728,6.72129 -3.10961,20.13805 0.59933,26.13929 2.48834,4.02618 7.34711,5.47786 11.86478,3.54488 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPathC() {
        try {
            GeneralPath path = SVGPathParser.parsePath(new PushbackInputStream(new ByteArrayInputStream("M 0,36 c -11.40916,-2.98919 -20.20567,-10.90776 -23.89181,-21.50729 -2.55372,-7.34322 -2.88713,-17.2322 -0.83998,-24.91464 1.31894,-4.9498 5.36674,-11.75143 9.39895,-15.79335 4.34147,-4.3519 10.97437,-8.01407 17.55198,-9.69081 7.72573,-1.96943 18.59748,-1.07488 26.11874,2.1491 2.71738,1.16479 2.72189,1.16479 4.36996,-0.009 0.90741,-0.64611 2.55724,-1.17475 3.66637,-1.17475 l 2.0166,0 0.38302,9.23541 c 0.21069,5.07948 0.3839,10.88459 0.38502,12.90026 l 0,3.66484 -2.06487,0 c -1.69908,0 -2.18372,-0.28567 -2.73567,-1.61253 -4.7721,-11.47201 -8.68744,-17.10322 -13.65586,-19.64034 -3.03338,-1.54901 -9.33363,-2.02998 -13.05395,-0.99655 -3.96483,1.10134 -8.89009,6.09651 -10.66804,10.81943 -2.23644,5.94094 -3.05812,11.21008 -3.06252,19.63925 -0.008,15.32522 4.18472,25.48188 12.10097,29.31406 5.4269,2.62712 13.38976,2.61106 18.86205,-0.0381 3.37494,-1.63377 8.49306,-6.34 10.84912,-9.97597 1.02691,-1.58484 2.02059,-2.88054 2.20817,-2.87933 0.18758,10e-4 1.20717,0.53028 2.26581,1.17571 2.20178,1.34243 2.20248,1.33936 -1.69087,7.10187 -4.0613,6.01113 -10.6752,10.64958 -17.84956,12.51826 -4.91769,1.28091 -15.22998,1.13827 -20.66557,-0.28583 z".getBytes())));
            path.transform(AffineTransform.getScaleInstance(0.2, 0.2));
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }

    public static GeneralPath createPathAllaBreve() {
        try {
            GeneralPath path = createPathC();
            path.append(new Rectangle2D.Float(-0.75f, -(float)LINES_SPACE, 0.75f, (float)LINES_SPACE), false);
            return path;
        } catch (Exception e) {
            throw new Error("exception while parsing path", e);
        }
    }
}