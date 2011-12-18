package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.Color;
import java.util.Random;

public class ManyNodesExample extends PFrame {

    private static final Random RANDOM = new Random();

    private static final int N = 100000;

    public static void main(String[] args) {
        new ManyNodesExample();
    }

    @Override
    public void initialize() {
        setSize(1200, 600);
        validate();
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        for (int i = 0; i < N; ++i) {
            PPath c = PPath.createEllipse(RANDOM.nextInt(8000), RANDOM.nextInt(8000), 40, 40);
            c.setStrokePaint(null);
            c.setPaint(new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255)));
            layer.addChild(c);
        }
    }

}