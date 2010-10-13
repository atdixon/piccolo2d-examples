package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LineBendWithArrowsExample extends LineBendExample {

    private static final double EPSILON = 0.001;
    private static final int ARROW_DIM = 20;

    public static void main(String[] args) {
        new LineBendWithArrowsExample();
    }

    private PPath a1, a2;
    
    @Override
    public void initialize() {
        super.initialize();

        a1 = arrow(ARROW_DIM, ARROW_DIM);
        a2 = arrow(ARROW_DIM, ARROW_DIM);

        layer.addChild(a1);
        layer.addChild(a2);
        
        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                a1.setVisible(dist(n1, k1) > EPSILON);
                a2.setVisible(dist(n2, k2) > EPSILON);
                if (a1.getVisible()) {
                    rebuildArrow(n1, k1, a1);
                }
                if (a2.getVisible()) {
                    rebuildArrow(n2, k2, a2);
                }
            }
        };

        k1.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);
        k2.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);

    }

    private void rebuildArrow(PNode n, PNode k, PPath a) {
        Point2D p = n.getFullBounds().getCenter2D();
        Point2D c = k.getFullBounds().getCenter2D();

        double x = c.getX() - p.getX();
        double y = c.getY() - p.getY();

        Point2D t = makeDistanceFromOrigin(x, y, DIAM / 2);

        double r = Math.atan2(y, x);

        a.setOffset(p.getX() + t.getX(), p.getY() + t.getY());
        a.setRotation(r);
    }

    private PPath arrow(int height, int width) {
        final PPath arrow = new PPath();
        arrow.moveTo(0, 0);
        arrow.lineTo(width, (float) - height / 2f);
        arrow.lineTo(width, (float) height / 2f);
        arrow.lineTo(0, 0);
        arrow.setPaint(Color.white);
        return arrow;
    }

}
