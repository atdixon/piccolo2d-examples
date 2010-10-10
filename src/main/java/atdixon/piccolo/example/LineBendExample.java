package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PRoot;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LineBendExample extends PFrame {

    private static final int DIAM = 25;
    private static final int QUANTUM = 200;

    public static void main(String[] args) {
        new LineBendExample();
    }

    @Override
    public void initialize() {
        setSize(800, 600);
        PCanvas canvas = getCanvas();
        PRoot root = canvas.getRoot();
        PLayer layer = canvas.getLayer();

        final PPath line = PPath.createLine(0, 0, 0, 0);

        final PPath c1 = PPath.createEllipse(0, 0, DIAM, DIAM);
        final PPath c2 = PPath.createEllipse(0, 0, DIAM, DIAM);

        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                line.reset();
                Point2D p1 = c1.getFullBounds().getCenter2D();
                Point2D p2 = c2.getFullBounds().getCenter2D();
                if (dist(p1, p2) < QUANTUM) {
                    line.setStrokePaint(Color.green);
                    buildCurve(p1, p2, line);
                } else {
                    // just draw straight line
                    line.setStrokePaint(Color.black);
                    buildLine(p1, p2, line);
                }
            }
        };

        c1.translate(200, 275);
        c2.translate(600, 275);

        c1.addPropertyChangeListener(l);
        c2.addPropertyChangeListener(l);

        layer.addChild(line);
        layer.addChild(c1);
        layer.addChild(c2);

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.addInputEventListener(new PDragEventHandler());

    }

    private void buildCurve(Point2D p1, Point2D p2, PPath line) {
        float x3 = 0;
        float y3 = 0;

        float x4 = 0;
        float y4 = 0;

        line.moveTo((float) p1.getX(), (float) p1.getY());
        line.curveTo(x3, y3, x4, y4, (float) p2.getX(), (float) p2.getY());
    }

    private void buildLine(Point2D p1, Point2D p2, PPath line) {
        line.moveTo((float) p1.getX(), (float) p1.getY());
        line.lineTo((float) p2.getX(), (float) p2.getY());
    }

    private double dist(Point2D one, Point2D two) {
        return Math.sqrt(Math.pow(one.getX() - two.getX(), 2) + Math.pow(one.getY() - two.getY(), 2));
    }

}
