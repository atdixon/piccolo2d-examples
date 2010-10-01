package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AnimatePPathExample extends PFrame {

    public static void main(String[] args) {
        new AnimatePPathExample();
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        // add bezier first so it's rendered "under" nodes
        final PPath bezier = new PPath();
        layer.addChild(bezier);
        // create first node
        final PPath node1 = PPath.createEllipse(0f, 0f, 50f, 50f);
        layer.addChild(node1);
        // create second node
        final PPath node2 = PPath.createEllipse(0f, 0f, 50f, 50f);
        layer.addChild(node2);

        PropertyChangeListener locator = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                bezier.reset();
                PBounds b1 = node1.getFullBounds();
                PBounds b2 = node2.getFullBounds();
                Point2D c1 = b1.getCenter2D();
                Point2D c2 = b2.getCenter2D();
                double half = Math.abs(c1.getX() - c2.getX()) / 2;
                Point2D i1 = new Point2D.Double(Math.min(c1.getX(), c2.getX()) + half, Math.min(b1.getY(), b2.getY()));
                bezier.moveTo((float) c1.getX(), (float) c1.getY());
                bezier.curveTo((float) i1.getX(), (float) i1.getY(), (float) i1.getX(), (float) i1.getY(), (float) c2.getX(), (float) c2.getY());
            }
        };

        node1.addPropertyChangeListener("bounds", locator);
        node1.addPropertyChangeListener("transform", locator);
        node2.addPropertyChangeListener("bounds", locator);
        node2.addPropertyChangeListener("transform", locator);

        node1.offset(0, 0);
        node2.offset(200, 0);

        node2.animateToBounds(200, 0, 50, 50, 3000);

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.addInputEventListener(new PDragEventHandler());
    }
    
}
