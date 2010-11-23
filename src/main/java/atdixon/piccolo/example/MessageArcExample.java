package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PDimension;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

public class MessageArcExample extends AbstractBezierExample {

    private static final Color lightBlue     = new Color(100, 200, 255);
    private static final Color veryLightGray = new Color(210, 210, 210);

    private static final float DIAM = 20;

    private static final double[] R = { 75, 75 }; // initial control point distances
    private static final double[] A = { Math.PI / 4, Math.PI / 6 }; // intial angle
    private static final double   F = 200; // field (horiz. distance under which line arcs)
    private static final double   N = 50; // natural horiz separation between

    private static final double ARROW_LENGTH = 30;
    private static final double ARROW_WIDTH = 30;

    public static void main(String[] args) {
        new MessageArcExample();
    }

    private PLayer layer;

    private PPath h1, h2; // handles
    private PPath l1, l2; // lines
    private PPath n1, n2; // ends
    private PPath arc; // message
    private PPath k1, k2; // controls
    private PPath a1; // arrows

    @Override
    public void initialize() {
        setSize(800, 600);
        setResizable(false);
        final PCanvas canvas = getCanvas();
        layer = canvas.getLayer();

        addEnds();
        addControls();
        addLines();
        addMessage();
        addHandles();
        addArrows();

        PDragEventHandler drag = new PDragEventHandler() {
            // override drag to only permit horizontal dragging
            protected void drag(final PInputEvent event) {
                final PDimension d = event.getDeltaRelativeTo(getDraggedNode());
                getDraggedNode().localToParent(d);
                getDraggedNode().offset(d.getWidth(), 0);
                event.setHandled(true);
                redrawAll();
            }
        };
        h1.addInputEventListener(drag);
        h2.addInputEventListener(drag);

        redrawAll();
    }

    private void redrawAll() {
        l1.setOffset(h1.getXOffset() + h1.getWidth() / 2, l1.getYOffset());
        l2.setOffset(h2.getXOffset() + h2.getWidth() / 2, l2.getYOffset());

        n1.setOffset(h1.getXOffset(), n1.getYOffset());
        n2.setOffset(h2.getXOffset(), n2.getYOffset());

        Point2D c1 = n1.getFullBounds().getCenter2D();
        Point2D c2 = n2.getFullBounds().getCenter2D();

        double dist = Math.abs(n2.getXOffset() - N - n1.getXOffset());
        double t = dist < F ? dist / F : 1;

        Point2D t1 = translate(rotate(new Point2D.Double(R[0] * (1 - t), 0), -A[0]), c1);
        Point2D t2 = translate(rotate(new Point2D.Double(R[1], 0), -A[1]), clipX(c2, c1));
        t2 = translate(
            makeDistanceFromOrigin(
                t2.getX() - c1.getX(),
                t2.getY() - c1.getY(),
                dist(t2, c1) * (1 - t)),
            c1);

        k1.centerBoundsOnPoint(t1.getX(), t1.getY());
        k2.centerBoundsOnPoint(t2.getX(), t2.getY());

        arc.reset();
        arc.moveTo((float) c1.getX(), (float) c1.getY());
        arc.curveTo((float) t1.getX(), (float) t1.getY(),
                    (float) t2.getX(), (float) t2.getY(),
                    (float) c2.getX(), (float) c2.getY());

        redrawArrows(c1.getX(), c1.getY(),
                     t1.getX(), t1.getY(),
                     t2.getX(), t2.getY(),
                     c2.getX(), c2.getY());
    }

    private PPath redrawArrows(double... curve) {
        // curve endpoint
        double xe = curve[6];
        double ye = curve[7];

        double[] sub = subdivideUntil(curve, ARROW_LENGTH);

        double x1 = sub[0];
        double y1 = sub[1];
        double x2 = sub[2];
        double y2 = sub[3];
        double x3 = sub[4];
        double y3 = sub[5];
        double x4 = sub[6];
        double y4 = sub[7];

        a1.reset();
        a1.moveTo((float) x1, (float) y1);
        a1.curveTo((float) x2, (float) y2,
                   (float) x3, (float) y3,
                   (float) x4, (float) y4);

        a1.setStrokePaint(Color.red);

        layer.addChild(a1);

        return a1;
    }

    private double[] subdivideUntil(double[] curve, double distance) {
        if (dist(curve[0], curve[1], curve[6], curve[7]) <= distance) {
            return curve;
        }
        double[] left = new double[8],
                 right = new double[8];
        CubicCurve2D.subdivide(curve, 0, left, 0, right, 0);
        return subdivideUntil(right, distance);
    }

    private void addHandles() {
        layer.addChild(h1 = PPath.createRectangle(0, 0, DIAM, DIAM));
        layer.addChild(h2 = PPath.createRectangle(0, 0, DIAM, DIAM));

        h1.offset(300, 100);
        h2.offset(400, 100);
    }

    private void addArrows() {
        layer.addChild(a1 = new PPath());
        a1.setStrokePaint(Color.red);
    }

    private void addMessage() {
        layer.addChild(arc = new PPath());
        arc.setPickable(false);
    }

    private void addLines() {
        l1 = PPath.createLine(0, 0, 0, 800);
        l1.setStrokePaint(Color.lightGray);
        layer.addChild(l1);
        l2 = PPath.createLine(0, 0, 0, 800);
        l2.setStrokePaint(Color.lightGray);
        layer.addChild(l2);
        l1.setPickable(false);
        l2.setPickable(false);
    }

    private void addControls() {
        layer.addChild(k1 = PPath.createEllipse(0, 0, DIAM / 2, DIAM / 2));
        layer.addChild(k2 = PPath.createEllipse(0, 0, DIAM / 2, DIAM / 2));
        k1.setStrokePaint(lightBlue);
        k2.setStrokePaint(lightBlue);
        k1.setPickable(false);
        k2.setPickable(false);
    }

    private void addEnds() {
        layer.addChild(n1 = PPath.createEllipse(0, 0, DIAM, DIAM));
        layer.addChild(n2 = PPath.createEllipse(0, 0, DIAM, DIAM));
        n1.setStrokePaint(veryLightGray);
        n2.setStrokePaint(veryLightGray);
        n1.setPickable(false);
        n2.setPickable(false);
        n1.offset(0, 225);
        n2.offset(0, 325);
    }

}
