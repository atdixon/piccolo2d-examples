package atdixon.piccolo.example.incubator;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.event.PSelectionEventHandler;
import edu.umd.cs.piccolox.pswing.PSwing;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

import javax.swing.JTextField;

public class ResizeNodesExample extends PFrame {

    public static void main(String[] args) {
        new ResizeNodesExample();
    }

    public ResizeNodesExample() {
        super("ResizeNodesExample", false, new PSwingCanvas());
    }

    @Override
    public void initialize() {
        final PSwingCanvas canvas = (PSwingCanvas) getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        final PText text = new PText("Hello World");
        text.setConstrainHeightToTextHeight(false);
        text.setConstrainWidthToTextWidth(false);
        layer.addChild(text);

        JTextField textField = new JTextField(20);
        PSwing swing = new PSwing(textField);
        layer.addChild(swing);

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.removeInputEventListener(canvas.getZoomEventHandler());
        canvas.addInputEventListener(new PSelectionEventHandler(layer, layer));

    }

}