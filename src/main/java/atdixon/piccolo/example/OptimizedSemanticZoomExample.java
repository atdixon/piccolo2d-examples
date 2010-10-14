package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.nodes.PNodeCache;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ListIterator;
import java.util.Random;

public class OptimizedSemanticZoomExample extends PFrame {

    private static final Dimension SCREEN = new Dimension(800, 600);
    
    private static final int NODES = 1000;
    private static final int DIAM = 40;

    public static void main(String[] args) {
        new OptimizedSemanticZoomExample();
    }

    private final Random random = new Random();

    private PCamera camera;

    @Override
    public void initialize() {
        setSize(SCREEN);

        final PCanvas canvas = getCanvas();
        camera = canvas.getCamera();
        final PLayer layer = canvas.getLayer();

        final PNodeCache nc = new PNodeCache();
        layer.addChild(nc);

        for (int i = 0; i < NODES; ++i) {
            ZoomAwareCircle c = new ZoomAwareCircle();
            c.offset(random.nextInt((int) SCREEN.getWidth()),
                     random.nextInt((int) SCREEN.getHeight()));
            nc.addChild(c);
        }

        camera.addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, new PropertyChangeListener() {

            private boolean zoomed = false;

            public void propertyChange(PropertyChangeEvent e) {
                // detect when we "cross" the zoom scale threshold and invalidate the node cache
                // so that it repaints itself
                boolean test = camera.getViewScale() < 1.5;
                if (test != zoomed) {
                    nc.invalidateCache();
                    zoomed = test;
                };
            }
        });
    }

    private final class ZoomAwareCircle extends PPath {

        ZoomAwareCircle() {
            setPathToEllipse(0, 0, DIAM, DIAM);
            Point2D m = getBounds().getCenter2D();
            
            PPath c1 = PPath.createEllipse(0, 0, DIAM / 2, DIAM / 2);
            PPath c2 = PPath.createEllipse(0, 0, DIAM / 2, DIAM / 2);
            addChild(c1);
            addChild(c2);

            c1.centerBoundsOnPoint(m.getX(), m.getY());
            c1.translate(DIAM / 4, 0);
            c2.centerBoundsOnPoint(m.getX(), m.getY());
            c2.translate(- DIAM / 4, 0);
        }

        @Override
        protected void paint(PPaintContext pc) {
            // use camera view scale instead of paint context
            setChildrenVisibility(camera.getViewScale() > 1.5);
            super.paint(pc);
        }

        private void setChildrenVisibility(boolean v) {
            ListIterator children = getChildrenIterator();
            while (children.hasNext()) {
                ((PNode) children.next()).setVisible(v);
            }
        }

    }

}
