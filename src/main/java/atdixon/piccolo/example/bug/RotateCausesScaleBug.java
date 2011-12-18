package atdixon.piccolo.example.bug;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;

public class RotateCausesScaleBug extends PFrame {

    public static void main(String[] args) {
        new RotateCausesScaleBug();
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        PNode n = new PNode();
        n.setBounds(0, 0, 50, 50);
        n.setOffset(100, 100);
        n.setPaint(Color.red);
        layer.addChild(n);

        n.animateToPositionScaleRotation(100, 100, 1,  - Math.PI / 4, 5000);
//
//        PAffineTransform t = n.getTransform();
//        t.setRotation(Math.PI);
//        PTransformActivity a = n.animateToTransform(t, 5000);
//        a.setLoopCount(Integer.MAX_VALUE);
//        n.addActivity(a);

    }

}