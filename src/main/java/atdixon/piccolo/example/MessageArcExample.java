package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PDimension;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MessageArcExample extends AbstractBezierExample {

    private static final float DIAM = 20;

    private static final double[] R = { 75, 75 }; // initial control point distances
    private static final double[] A = { Math.PI / 4, Math.PI / 6 }; // intial angle
    private static final double   F = 200; // field (horiz. distance under which line arcs)
    private static final double   N = 50; // natural horiz separation between

    public static void main(String[] args) {
        new MessageArcExample();
    }

    private PPath n1, n2, arc;
    private PPath k1, k2;

    @Override
    public void initialize() {
        setSize(800, 600);
        setResizable(false);
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        layer.addChild(k1 = PPath.createEllipse(0, 0, DIAM + 5, DIAM + 5));
        layer.addChild(k2 = PPath.createEllipse(0, 0, DIAM + 5, DIAM + 5));
        k1.setStrokePaint(Color.blue);
        k2.setStrokePaint(Color.blue);

        final PPath l1 = PPath.createLine(0, 0, 0, 800);
        l1.setStrokePaint(Color.lightGray);
        layer.addChild(l1);
        final PPath l2 = PPath.createLine(0, 0, 0, 800);
        l2.setStrokePaint(Color.lightGray);
        layer.addChild(l2);

        layer.addChild(arc = new PPath());

        layer.addChild(n1 = PPath.createEllipse(0, 0, DIAM, DIAM));
        layer.addChild(n2 = PPath.createEllipse(0, 0, DIAM, DIAM));

        l1.setPickable(false);
        l2.setPickable(false);
        arc.setPickable(false);

        PropertyChangeListener n = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                l1.setOffset(n1.getXOffset() + n1.getWidth() / 2, l1.getYOffset());
                l2.setOffset(n2.getXOffset() + n2.getWidth() / 2, l2.getYOffset());

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
            }
        };

        n1.addPropertyChangeListener(n);
        n2.addPropertyChangeListener(n);

        n1.offset(350, 225);
        n2.offset(350 + N, 325);

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.removeInputEventListener(canvas.getZoomEventHandler());
        canvas.addInputEventListener(new PDragEventHandler() {
            // override drag to only permit horizontal dragging
            protected void drag(final PInputEvent event) {
                final PDimension d = event.getDeltaRelativeTo(getDraggedNode());
                getDraggedNode().localToParent(d);
                getDraggedNode().offset(d.getWidth(), 0);
            }
        });
    }

}
