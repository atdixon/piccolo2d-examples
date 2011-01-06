package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PropertyChangeParentMaskExample extends PFrame {

    public static void main(String[] args) {
        new PropertyChangeParentMaskExample();
    }

    private final PPath link = new PPath();

    @Override
    public void initialize() {
        PCanvas canvas = getCanvas();
        PLayer layer = canvas.getLayer();

        final Bullseye b1 = new Bullseye();
        final Bullseye b2 = new Bullseye();

        b1.offset(100, 100);

        layer.addChild(b1);
        layer.addChild(b2);
        layer.addChild(link);

        PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // update the link to connect the two inner most nodes
                PNode n1 = b1.getInnerMostNode();
                PNode n2 = b2.getInnerMostNode();
                Point2D p1 = n1.getGlobalFullBounds().getCenter2D();
                Point2D p2 = n2.getGlobalFullBounds().getCenter2D();
                final Line2D line =
                    new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                link.setPathTo(line);
            }
        };
        b1.addPropertyChangeListener(listener);
        b2.addPropertyChangeListener(listener);

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.addInputEventListener(new PDragEventHandler());
    }

    static class Bullseye extends PNode {

        private PNode innerMostNode;

        Bullseye() {
            int size = 100;
            innerMostNode = this;
            for (int i = 0; i < 5; ++i) {
                PPath curr = PPath.createEllipse(0, 0, size, size);
                Point2D c = innerMostNode.getBounds().getCenter2D();
                curr.centerBoundsOnPoint(c.getX(), c.getY());
                curr.setPropertyChangeParentMask(PNode.PROPERTY_CODE_FULL_BOUNDS);
                innerMostNode.addChild(curr);
                size -= 15;
                innerMostNode = curr;
            }
        }

        public PNode getInnerMostNode() {
            return innerMostNode;
        }

    }

}