package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.event.PSelectionEventHandler;

import java.awt.*;

public class SimpleSelectionExample extends PFrame {

    public static void main(String[] args) {
        new SimpleSelectionExample();
    }

    @Override
    public void initialize() {
        setSize(800, 600);
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        for (int i = 0; i < 10; ++i) {
            PNode n = new PNode();
            n.setPaint(new Color(255 - i * 25, i * 15, i * 10));
            n.setBounds(0, 0, 50, 50);
            n.offset(i * 50, i * 50);
            layer.addChild(n);
        }

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.removeInputEventListener(canvas.getZoomEventHandler());

        PSelectionEventHandler selectionEventHandler = new PSelectionEventHandler(layer, layer);
        canvas.addInputEventListener(selectionEventHandler);
    }

}