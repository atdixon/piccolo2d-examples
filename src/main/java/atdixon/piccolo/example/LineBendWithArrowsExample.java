package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LineBendWithArrowsExample extends LineBendExample {

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
                Point2D p1 = n1.getFullBounds().getCenter2D();
                Point2D c1 = k1.getFullBounds().getCenter2D();

                Point2D p2 = n2.getFullBounds().getCenter2D();
                Point2D c2 = k2.getFullBounds().getCenter2D();

                double d1 = toPolarAngle(c1.getX() - p1.getX(), c1.getY() - p1.getY());
                double d2 = toPolarAngle(c2.getX() - p2.getX(), c2.getY() - p2.getY());

                Point2D t1 = makeDistanceFromOrigin(c1.getX() - p1.getX(), c1.getY() - p1.getY(), DIAM / 2);
                Point2D t2 = makeDistanceFromOrigin(c2.getX() - p2.getX(), c2.getY() - p2.getY(), DIAM / 2);


                a1.setOffset(p1.getX() + t1.getX(), p1.getY() + t1.getY());
                a2.setOffset(p2.getX() + t2.getX(), p2.getY() + t2.getY());

                a1.setRotation(- d1);
                a2.setRotation(- d2);
            }
        };

        k1.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);
        k2.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);

    }

    private double toPolarAngle(double x, double y) {
        double d = Math.atan(Math.abs(y) / Math.abs(x));
        int q = toQuadrant(x, y);
        switch (q) {
            case 0:
                return Math.toRadians(360) - d;
            case 1:
                return Math.toRadians(180) + d;
            case 2:
                return Math.toRadians(180) - d;
            case 3:
                return d;
            default:
                throw new IllegalStateException(String.valueOf(q));
        }
    }

    private int toQuadrant(double x, double y) {
        return (y < 0 ? 2 : 0) + (y < 0 != x < 0 ? 1 : 0);
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
