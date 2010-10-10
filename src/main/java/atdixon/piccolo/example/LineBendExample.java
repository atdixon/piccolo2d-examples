package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PRoot;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LineBendExample extends PFrame {

    private static final int DIAM = 25;
    private static final int STRESS = 200;
    private static final int QUANTUM = 25;
    private static final int MAX_BEND = 160; // in degrees

    public static void main(String[] args) {
        new LineBendExample();
    }

    private PPath line, c1, c2, km, k1, k2;

    @Override
    public void initialize() {
        setSize(800, 600);
        PCanvas canvas = getCanvas();
        PRoot root = canvas.getRoot();
        PLayer layer = canvas.getLayer();

        line = PPath.createLine(0, 0, 0, 0);
        line.setStrokePaint(Color.green);

        c1 = PPath.createEllipse(0, 0, DIAM, DIAM);
        c2 = PPath.createEllipse(0, 0, DIAM, DIAM);

        km = PPath.createEllipse(0, 0, DIAM, DIAM);
        km.setStrokePaint(Color.red);
        k1 = PPath.createEllipse(0, 0, DIAM, DIAM);
        k1.setStrokePaint(Color.blue);
        k2 = PPath.createEllipse(0, 0, DIAM, DIAM);
        k2.setStrokePaint(Color.blue);
        
        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                rebuildCurve();
            }
        };

        c1.translate(200, 275);
        c2.translate(600, 275);

        c1.addPropertyChangeListener(l);
        c2.addPropertyChangeListener(l);

        layer.addChild(line);
        layer.addChild(c1);
        layer.addChild(c2);

        layer.addChild(km);
        layer.addChild(k1);
        layer.addChild(k2);

        // add some helpful labels
        c1.addChild(text("c1"));
        c2.addChild(text("c2"));

        km.addChild(text("km"));
        k1.addChild(text("k1"));
        k2.addChild(text("k2"));

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        c1.addInputEventListener(new PDragEventHandler());
        c2.addInputEventListener(new PDragEventHandler());
    }

    private void rebuildCurve() {
        Point2D p1 = c1.getFullBounds().getCenter2D();
        Point2D p2 = c2.getFullBounds().getCenter2D();

        double dist = dist(p1, p2);
        Point2D mid = midpoint(p1, p2);

        // relative mids
        Point2D mc1 = new Point2D.Double(mid.getX() - p1.getX(), mid.getY() - p1.getY());
        Point2D mc2 = new Point2D.Double(mid.getX() - p2.getX(), mid.getY() - p2.getY());

        Point2D c1 = makeDistanceFromOrigin(mc1, STRESS / 2);
        Point2D c2 = makeDistanceFromOrigin(mc2, STRESS / 2);

        double t = dist > STRESS ? 1 : (dist / STRESS);

        boolean direction = p1.getX() < p2.getX();

        Point2D rc1 = rotate(c1, (1 - t) * Math.toRadians(direction ? -MAX_BEND : MAX_BEND));
        Point2D rc2 = rotate(c2, (1 - t) * Math.toRadians(direction ? MAX_BEND : -MAX_BEND));

        c1 = translate(rc1, p1);
        c2 = translate(rc2, p2);

        k1.centerFullBoundsOnPoint(c1.getX(), c1.getY());
        k2.centerFullBoundsOnPoint(c2.getX(), c2.getY());

        km.centerFullBoundsOnPoint(mid.getX(), mid.getY());

        float x3 = (float) c1.getX();
        float y3 = (float) c1.getY();

        float x4 = (float) c2.getX();
        float y4 = (float) c2.getY();

        line.reset();
        line.moveTo((float) p1.getX(), (float) p1.getY());
        line.curveTo(x3, y3, x4, y4, (float) p2.getX(), (float) p2.getY());
        line.lineTo((float) p2.getX(), (float) p2.getY());
    }

    private Point2D translate(Point2D p, Point2D t) {
        return new Point2D.Double(p.getX() + t.getX(), p.getY() + t.getY());
    }

    private Point2D rotate(Point2D p, double theta) {
        return new Point2D.Double(
            p.getX() * Math.cos(theta) - p.getY() * Math.sin(theta),
            p.getX() * Math.sin(theta) + p.getY() * Math.cos(theta)
        );
    }

    private Point2D makeDistanceFromOrigin(Point2D p, double dist) {
        double denom = Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2));
        return new Point2D.Double(
            dist * p.getX() / denom,
            dist * p.getY() / denom
        );
    }

    private Point2D midpoint(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() + (p2.getX() - p1.getX()) / 2,
                           p1.getY() + (p2.getY() - p1.getY()) / 2);
    }

    private double dist(Point2D one, Point2D two) {
        return Math.sqrt(Math.pow(one.getX() - two.getX(), 2) + Math.pow(one.getY() - two.getY(), 2));
    }

    private PText text(String s) {
        PText text = new PText(s);
        text.setPickable(false);
        return text;
    }
    

}
