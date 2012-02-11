package atdixon.piccolo.demo;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.activities.PInterpolatingActivity;
import edu.umd.cs.piccolo.nodes.PText;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

class Intro implements Step {

    private final PCanvas canvas;

    Intro(PCanvas canvas) {
        this.canvas = canvas;
    }

    public void run() throws Exception {
        final PLayer layer = canvas.getLayer();
        final PText p2d = new PText("Piccolo2D");
        p2d.setFont(new Font("Helvetica", Font.BOLD, 36));
        p2d.setTransparency(0f);
        final Point2D center = canvas.getCamera().getViewBounds().getCenter2D();
        final Object latch = new Object();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                layer.addChild(p2d);
                p2d.centerFullBoundsOnPoint(center.getX(), center.getY());
                PInterpolatingActivity at = p2d.animateToTransparency(1f, 2000);
                PActivity ap = new PInterpolatingActivity(1500) {
                    @Override
                    public void setRelativeTargetValue(float zeroToOne) {
                        p2d.setScale(1 + zeroToOne * 400);
                        p2d.centerFullBoundsOnPoint(center.getX(), center.getY());
                    }
                };
                ap.startAfter(at);
                layer.getRoot().addActivity(ap);
                Util.notifyAfter(latch, ap);
            }
        });
        synchronized (latch) {
            latch.wait();
        }
        Util.clear(layer);
    }

}
