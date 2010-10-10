package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PRoot;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LineBendExample extends PFrame {

    private static final int DIAM = 10;

    public static void main(String[] args) {
        new LineBendExample();
    }

    @Override
    public void initialize() {
        PCanvas canvas = getCanvas();
        PRoot root = canvas.getRoot();
        PLayer layer = canvas.getLayer();

        final PPath line = PPath.createLine(0, 0, 0, 0);

        final PPath p1 = PPath.createEllipse(0, 0, DIAM, DIAM);
        final PPath p2 = PPath.createEllipse(0, 0, DIAM, DIAM);

        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                line.reset();
                Point2D b1 = p1.getFullBounds().getCenter2D();
                Point2D b2 = p2.getFullBounds().getCenter2D();
                line.moveTo((float) b1.getX(), (float) b1.getY());
                line.lineTo((float) b2.getX(), (float) b2.getY());
            }
        };

        p1.addPropertyChangeListener(l);
        p2.addPropertyChangeListener(l);

        layer.addChild(line);
        layer.addChild(p1);
        layer.addChild(p2);

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.addInputEventListener(new PDragEventHandler());

    }

}
