package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;
import java.util.Random;

public class ViewConstraintExample extends PFrame {

    public static void main(String[] args) {
        new ViewConstraintExample();
    }

    private final Random random = new Random();

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        for (int i = 0; i < 200; ++i) {
            PNode n = new PNode();
            n.setBounds(0, 0, 10, 10);
            int c = random.nextInt(255);
            n.setPaint(new Color(c / 2, c, c / 2));
            n.offset((255 - c) * 4,
                    random.nextInt(1000));
            layer.addChild(n);
        }

        camera.setViewConstraint(PCamera.VIEW_CONSTRAINT_ALL);
    }

}