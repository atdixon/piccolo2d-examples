package atdixon.piccolo.example.incubator;

import atdixon.piccolo.example.support.CustomNodeCache;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.PFrame;

import java.awt.Color;
import java.util.Random;

public class CacheWhenAfarExample extends PFrame {

    private static final int RECT_DIM = 25;

    private static final Random RANDOM = new Random();
    private static final int NUM_RECTS = 1000;

    public static void main(String[] args) {
        new CacheWhenAfarExample();
    }

    @Override
    public void initialize() {
        setSize(800, 600);
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        layer.addChild(new Rectangles());
    }

    private final class Rectangles extends CustomNodeCache {

        Rectangles() {
            for (int i = 0; i < NUM_RECTS; ++i) {
                final PPath r = PPath.createRectangle(0, 0, RECT_DIM, RECT_DIM);
                r.offset(RANDOM.nextInt(CacheWhenAfarExample.this.getWidth()),
                         RANDOM.nextInt(CacheWhenAfarExample.this.getHeight()));
                r.setPaint(new Color(RANDOM.nextInt(0xffffff)));
                addChild(r);
            }
        }

        @Override
        public boolean isNodeCacheEnabled(PPaintContext pc) {
            return pc.getScale() < 0.5;
        }

    }

}