package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Double-click to create a curve; drag curve using red midpoint; double-click red midpoint to split the curve.
 */
public class BezierEditorExample extends AbstractBezierExample {

    private static final int DIAM = 10;
    private static final int KDIAM = 10;
    private static final int QUANTUM = 20;

    private static final double[] INIT = { 0, 100, 0, 0, 200, 0, 200, 100 };

    public static void main(String[] args) {
        new BezierEditorExample();
    }

    private PLayer layer;

    @Override
    public void initialize() {
        setSize(800, 600);
        final PCanvas canvas = getCanvas();
        layer = canvas.getLayer();

        canvas.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent event) {
                if (event.getClickCount() == 2) {
                    EditableBezier bezier = new EditableBezier(INIT);
                    bezier.setOffset(event.getPosition());
                    layer.addChild(bezier);
                }
                event.setHandled(true);
            }
        });
    }

    private class EditableBezier extends PNode {

        private PPath line, c1, c2, km, k1, k2, l1, l2;

        EditableBezier(double[] curve) {
            line = PPath.createLine(0, 0, 0, 0);
            line.setPaint(null);
            line.setStrokePaint(Color.green);

            c1 = PPath.createRectangle((float) curve[0], (float) curve[1], DIAM, DIAM);
            c2 = PPath.createRectangle((float) curve[6], (float) curve[7], DIAM, DIAM);

            km = PPath.createEllipse(0, 0, DIAM, DIAM);
            km.setStrokePaint(Color.red);
            
            k1 = PPath.createEllipse((float) curve[2], (float) curve[3], KDIAM, KDIAM);
            k1.setStrokePaint(Color.blue);
            k2 = PPath.createEllipse((float) curve[4], (float) curve[5], KDIAM, KDIAM);
            k2.setStrokePaint(Color.blue);

            l1 = PPath.createLine(0, 0, 0, 0);
            l1.setStrokePaint(Color.lightGray);
            l2 = PPath.createLine(0, 0, 0, 0);
            l2.setStrokePaint(Color.lightGray);

            PropertyChangeListener redraw = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    redrawCurve();
                }
            };

            c1.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, redraw);
            c2.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, redraw);
            k1.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, redraw);
            k2.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, redraw);

            addChild(line);
            addChild(l1);
            addChild(l2);

            addChild(km);
            addChild(k1);
            addChild(k2);

            addChild(c1);
            addChild(c2);

            PDragEventHandler dragWithRepel = new PDragEventHandler() {
                @Override
                protected void drag(PInputEvent event) {
                    if (dist(c1, c2) > QUANTUM) {
                        super.drag(event);
                    } else {
                        PNode d = getDraggedNode();
                        PNode o = d == c1 ? c2 : c1;
                        repel(d, o);
                    }
                    event.setHandled(true);
                }
            };

            c1.addInputEventListener(dragWithRepel);
            c2.addInputEventListener(dragWithRepel);
            k1.addInputEventListener(dragWithRepel);
            k2.addInputEventListener(dragWithRepel);

            PDragEventHandler dragWhole = new PDragEventHandler() {
                @Override
                protected void startDrag(PInputEvent event) {
                    super.startDrag(event);
                    setDraggedNode(EditableBezier.this);
                }
                @Override
                protected void drag(PInputEvent event) {
                    super.drag(event);
                    event.setHandled(true);
                }
            };

            PBasicInputEventHandler split = new PBasicInputEventHandler() {
                @Override
                public void mouseClicked(PInputEvent event) {
                    if (event.getClickCount() == 2) {
                        double[] left = new double[8], right = new double[8];
                        CubicCurve2D.subdivide(EditableBezier.this.toCurveArray(), 0, left, 0, right, 0);

                        EditableBezier l = new EditableBezier(left);
                        l.setOffset(EditableBezier.this.getOffset());
                        EditableBezier r = new EditableBezier(right);
                        r.setOffset(EditableBezier.this.getOffset());
                        layer.removeChild(EditableBezier.this);
                        layer.addChild(l);
                        layer.addChild(r);
                    }
                    event.setHandled(true);
                }
            };

            // let the midpoint drag the whole curve
            km.addInputEventListener(dragWhole);
            km.addInputEventListener(split);
        }

        private double[] toCurveArray() {
            // in local coordinates of this node
            Point2D c1_ = c1.getFullBounds().getCenter2D();
            Point2D c2_ = c2.getFullBounds().getCenter2D();
            Point2D k1_ = k1.getFullBounds().getCenter2D();
            Point2D k2_ = k2.getFullBounds().getCenter2D();
            return new double[] {
                c1_.getX(), c1_.getY(),
                k1_.getX(), k1_.getY(),
                k2_.getX(), k2_.getY(),
                c2_.getX(), c2_.getY()
            };
        }

        private void redrawCurve() {
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

    }

}
