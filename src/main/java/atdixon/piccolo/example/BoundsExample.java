package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;

/** Demonstrate local bounds, especially how they do not affect children. */
public class BoundsExample extends PFrame {

    public static void main(String[] args) {
        new BoundsExample();
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        // create a parent, p...
        PNode p = new PNode();
        p.setPaint(Color.red);
        // ...with *local* bounds...
        p.setBounds(100, 100, 100, 100);

        // create a child, c...
        PNode c = new PNode();
        c.setPaint(Color.green);
        // ...with *local* bounds...
        c.setBounds(0, 0, 100, 100);
        p.addChild(c);

        // add parent to layer, note that its local bounds do not affect the child
        layer.addChild(p);

        // add a blue path that demonstrates the full bounds of p
        PBounds pfb = p.getFullBounds();
        PPath path = new PPath(pfb);
        path.setStrokePaint(Color.blue);
        layer.addChild(path);
    }

}