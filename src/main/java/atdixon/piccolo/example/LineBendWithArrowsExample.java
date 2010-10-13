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

                double x1 = c1.getX() - p1.getX();
                double y1 = c1.getY() - p1.getY();

                double x2 = c2.getX() - p2.getX();
                double y2 = c2.getY() - p2.getY();

                Point2D t1 = makeDistanceFromOrigin(x1, y1, DIAM / 2);
                Point2D t2 = makeDistanceFromOrigin(x2, y2, DIAM / 2);
                
                double r1 = Math.atan2(y1, x1);
                double r2 = Math.atan2(y2, x2);

                a1.setOffset(p1.getX() + t1.getX(), p1.getY() + t1.getY());
                a2.setOffset(p2.getX() + t2.getX(), p2.getY() + t2.getY());

                a1.setRotation(r1);
                a2.setRotation(r2);
            }
        };

        k1.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);
        k2.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);

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
