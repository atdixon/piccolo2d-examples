package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.PRoot;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BezierEditorExample extends AbstractBezierExample {

    private static final int DIAM = 25;
    private static final int KDIAM = DIAM + 3;
    private static final int QUANTUM = 20;

    public static void main(String[] args) {
        new BezierEditorExample();
    }

    private PPath line, c1, c2, km, k1, k2, l1, l2;

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

        km = PPath.createEllipse(0, 0, KDIAM, KDIAM);
        km.setStrokePaint(Color.red);
        k1 = PPath.createEllipse(0, 0, KDIAM, KDIAM);
        k1.setStrokePaint(Color.blue);
        k2 = PPath.createEllipse(0, 0, KDIAM, KDIAM);
        k2.setStrokePaint(Color.blue);

        l1 = PPath.createLine(0, 0, 0, 0);
        l1.setStrokePaint(Color.lightGray);
        l2 = PPath.createLine(0, 0, 0, 0);
        l2.setStrokePaint(Color.lightGray);

        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Point2D p1 = c1.getFullBounds().getCenter2D();
                Point2D p2 = c2.getFullBounds().getCenter2D();

                Point2D mid = midpoint(p1, p2);

                km.centerFullBoundsOnPoint(mid.getX(), mid.getY());

                Point2D p3 = k1.getFullBounds().getCenter2D();
                Point2D p4 = k2.getFullBounds().getCenter2D();

                float x3 = (float) p3.getX();
                float y3 = (float) p3.getY();

                float x4 = (float) p4.getX();
                float y4 = (float) p4.getY();

                line.reset();
                line.moveTo((float) p1.getX(), (float) p1.getY());
                line.curveTo(x3, y3, x4, y4, (float) p2.getX(), (float) p2.getY());
                line.lineTo((float) p2.getX(), (float) p2.getY());

                l1.reset();
                l1.moveTo((float) p1.getX(), (float) p1.getY());
                l1.lineTo((float) p3.getX(), (float) p3.getY());
                l2.reset();
                l2.moveTo((float) p2.getX(), (float) p2.getY());
                l2.lineTo((float) p4.getX(), (float) p4.getY());
            }
        };

        c1.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);
        c2.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);
        k1.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);
        k2.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);

        c1.translate(200, 275);
        c2.translate(600, 275);

        layer.addChild(line);
        layer.addChild(l1);
        layer.addChild(l2);

        layer.addChild(km);
        layer.addChild(k1);
        layer.addChild(k2);

        layer.addChild(c1);
        layer.addChild(c2);

        canvas.removeInputEventListener(canvas.getPanEventHandler());

        PDragEventHandler drag = new PDragEventHandler() {
            @Override
            protected void drag(PInputEvent event) {
                if (dist(c1, c2) > QUANTUM) {
                    super.drag(event);
                } else {
                    PNode d = getDraggedNode();
                    PNode o = d == c1 ? c2 : c1;
                    repel(d, o);
                }
            }
        };

        c1.addInputEventListener(drag);
        c2.addInputEventListener(drag);
        k1.addInputEventListener(drag);
        k2.addInputEventListener(drag);
    }

}
