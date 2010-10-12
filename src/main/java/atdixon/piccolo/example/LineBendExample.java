package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LineBendExample extends AbstractBezierExample {

    protected static final int DIAM = 25;
    protected static final int KDIAM = DIAM + 3;
    protected static final int STRESS_FIELD = 250; // the node distance at which the line begins to bend
    protected static final int QUANTUM = 20; // don't let the two nodes get closer than this

    public static void main(String[] args) {
        new LineBendExample();
    }

    protected PCanvas canvas;
    protected PLayer layer;

    protected Strategy strategy = new RotationStrategy();

    protected PPath line, n1, n2, nm, k1, k2;

    @Override
    public void initialize() {
        setSize(800, 600);
        canvas = getCanvas();
        layer = canvas.getLayer();

        line = PPath.createLine(0, 0, 0, 0);
        line.setStrokePaint(Color.green);

        n1 = PPath.createEllipse(0, 0, DIAM, DIAM);
        n2 = PPath.createEllipse(0, 0, DIAM, DIAM);

        nm = PPath.createEllipse(0, 0, KDIAM, KDIAM);
        nm.setStrokePaint(Color.red);
        k1 = PPath.createEllipse(0, 0, KDIAM, KDIAM);
        k1.setStrokePaint(Color.blue);
        k2 = PPath.createEllipse(0, 0, KDIAM, KDIAM);
        k2.setStrokePaint(Color.blue);
        
        layer.addChild(line);

        layer.addChild(nm);
        layer.addChild(k1);
        layer.addChild(k2);

        layer.addChild(n1);
        layer.addChild(n2);

        // add some helpful labels
        n1.addChild(text("n1"));
        n2.addChild(text("n2"));

        nm.addChild(text("nm"));
        k1.addChild(text("k1"));
        k2.addChild(text("k2"));

        canvas.removeInputEventListener(canvas.getPanEventHandler());

        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                strategy.rebuildCurve();
            }
        };

        n1.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);
        n2.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);
        
        PDragEventHandler drag = new PDragEventHandler() {
            @Override
            protected void drag(PInputEvent event) {
                if (dist(n1, n2) > QUANTUM) {
                    super.drag(event);
                } else {
                    PNode d = getDraggedNode();
                    PNode o = d == n1 ? n2 : n1;
                    repel(d, o);
                }
            }
        };

        n1.addInputEventListener(drag);
        n2.addInputEventListener(drag);

        n1.translate(200, 275);
        n2.translate(600, 275);
    }

    protected PText text(String s) {
        PText text = new PText(s);
        text.setPickable(false);
        return text;
    }

    /** Strategy. */
    protected abstract class Strategy {

        abstract double controlPointRotation(double t);
        abstract double controlPointDistance(double t);

        void rebuildCurve() {
            Point2D p1 = n1.getFullBounds().getCenter2D();
            Point2D p2 = n2.getFullBounds().getCenter2D();

            double dist = dist(p1, p2);

            Point2D mid = midpoint(p1, p2);

            // relative mids
            Point2D mp1 = new Point2D.Double(mid.getX() - p1.getX(), mid.getY() - p1.getY());
            Point2D mp2 = new Point2D.Double(mid.getX() - p2.getX(), mid.getY() - p2.getY());

            double t = dist > STRESS_FIELD ? 1 : (dist / STRESS_FIELD);

            double cpd = controlPointDistance(t);

            Point2D c1 = makeDistanceFromOrigin(mp1, cpd);
            Point2D c2 = makeDistanceFromOrigin(mp2, cpd);

            double cpr = controlPointRotation(t);
            
            Point2D rc1 = rotate(c1, Math.toRadians(-cpr));
            Point2D rc2 = rotate(c2, Math.toRadians(cpr));

            c1 = translate(rc1, p1);
            c2 = translate(rc2, p2);

            k1.centerFullBoundsOnPoint(c1.getX(), c1.getY());
            k2.centerFullBoundsOnPoint(c2.getX(), c2.getY());

            nm.centerFullBoundsOnPoint(mid.getX(), mid.getY());

            float x3 = (float) c1.getX();
            float y3 = (float) c1.getY();

            float x4 = (float) c2.getX();
            float y4 = (float) c2.getY();

            line.reset();
            line.moveTo((float) p1.getX(), (float) p1.getY());
            line.curveTo(x3, y3, x4, y4, (float) p2.getX(), (float) p2.getY());
            line.lineTo((float) p2.getX(), (float) p2.getY());
        }

    }

    /** Use control points that maintain a constant angle wrt to the straight line between the two nodes. */
    protected class OrthogonalStrategy extends Strategy {

        private final int angle;

        public OrthogonalStrategy() {
            this(90);
        }

        public OrthogonalStrategy(int angle) {
            this.angle = angle;
        }

        @Override
        double controlPointRotation(double t) {
            return t > 1 ? 0 : angle;
        }

        @Override
        double controlPointDistance(double t) {
            return (1 - t) * STRESS_FIELD / 2;
        }

        @Override
        public String toString() {
            return "OrthogonalStrategy(" + angle + ")";
        }

    }

    /** Rotate the control points away from each other as the two nodes approach each other. */
    protected class RotationStrategy extends Strategy {

        private final int maxAngle;

        public RotationStrategy() {
            this(180);
        }

        public RotationStrategy(int maxAngle) {
            this.maxAngle = maxAngle;
        }

        @Override
        double controlPointRotation(double t) {
            return (1 - t) * maxAngle;
        }

        @Override
        double controlPointDistance(double t) {
            return STRESS_FIELD / 2;
        }

        @Override
        public String toString() {
            return "RotationStrategy(" + maxAngle + ")";
        }

    }

}
