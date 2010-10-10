package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.geom.Point2D;

public abstract class AbstractBezierExample extends PFrame {

    protected Point2D translate(Point2D p, Point2D t) {
        return new Point2D.Double(p.getX() + t.getX(), p.getY() + t.getY());
    }

    protected Point2D rotate(Point2D p, double theta) {
        return new Point2D.Double(
            p.getX() * Math.cos(theta) - p.getY() * Math.sin(theta),
            p.getX() * Math.sin(theta) + p.getY() * Math.cos(theta)
        );
    }

    protected Point2D makeDistanceFromOrigin(Point2D p, double dist) {
        double denom = Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2));
        return new Point2D.Double(
            dist * p.getX() / denom,
            dist * p.getY() / denom
        );
    }

    protected Point2D midpoint(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() + (p2.getX() - p1.getX()) / 2,
                           p1.getY() + (p2.getY() - p1.getY()) / 2);
    }

    protected void repel(PNode moveable, PNode still) {
        Point2D r1 = moveable.getFullBounds().getCenter2D();
        Point2D r2 = still.getFullBounds().getCenter2D();

        Point2D mid = midpoint(r1, r2);

        // relative mid
        Point2D m1 = new Point2D.Double(mid.getX() - r1.getX(), mid.getY() - r1.getY());

        Point2D c1 = makeDistanceFromOrigin(m1, -1);

        c1 = translate(c1, r1);

        moveable.centerFullBoundsOnPoint(c1.getX(), c1.getY());
    }

    protected double dist(PPath c1, PPath c2) {
        return dist(c1.getFullBounds().getCenter2D(), c2.getFullBounds().getCenter2D());
    }

    protected double dist(Point2D one, Point2D two) {
        return Math.sqrt(Math.pow(one.getX() - two.getX(), 2) + Math.pow(one.getY() - two.getY(), 2));
    }

}
