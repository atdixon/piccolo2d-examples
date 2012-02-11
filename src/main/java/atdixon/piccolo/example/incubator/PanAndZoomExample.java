package atdixon.piccolo.example.incubator;

import atdixon.piccolo.example.support.ScrollZoomEventHandler;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.PFrame;

import java.awt.Color;

public class PanAndZoomExample extends PFrame {

    public static void main(String[] args) {
        new PanAndZoomExample();
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        PNode node = new PNode();
        node.setPaint(Color.red);
        node.setBounds(0, 0, 100, 100);
        layer.addChild(node);

        canvas.removeInputEventListener(canvas.getZoomEventHandler());
        canvas.addInputEventListener(new ScrollZoomEventHandler());
    }

}