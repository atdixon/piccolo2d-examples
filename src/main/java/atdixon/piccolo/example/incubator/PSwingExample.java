package atdixon.piccolo.example.incubator;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.pswing.PSwing;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

import javax.swing.JTextField;
import java.awt.event.InputEvent;

public class PSwingExample extends PFrame {

    public static void main(String[] args) {
        new PSwingExample();
    }

    public PSwingExample() {
        super("PSwingExample", false, new PSwingCanvas());
    }

    @Override
    public void initialize() {
        PSwingCanvas canvas = (PSwingCanvas) getCanvas();
        PLayer layer = canvas.getLayer();

        JTextField tf1 = new JTextField(20);
        PSwing s1 = new PSwing(tf1);

        JTextField tf2 = new JTextField(20);
        PSwing s2 = new PSwing(tf2);

        s1.offset(100, 100);
        s2.offset(110, 100);

        layer.addChild(s1);
        layer.addChild(s2);

        PInputEventFilter pf1 = new PInputEventFilter(InputEvent.BUTTON1_MASK);
        PInputEventFilter pf2 = new PInputEventFilter(InputEvent.BUTTON3_MASK);
        pf1.setAcceptsAlreadyHandledEvents(true);
        pf2.setAcceptsAlreadyHandledEvents(true);
        canvas.getPanEventHandler().setEventFilter(pf1);
        canvas.getZoomEventHandler().setEventFilter(pf2);
    }

}