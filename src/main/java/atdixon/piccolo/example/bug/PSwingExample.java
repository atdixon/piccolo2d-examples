package atdixon.piccolo.example.bug;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.pswing.PSwing;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PSwingExample extends PFrame {

    public static void main(String[] args) {
        new PSwingExample();
    }

    public PSwingExample() {
        super(PSwingExample.class.getSimpleName(), false, new PSwingCanvas());
    }

    @Override
    public void initialize() {
        final PSwingCanvas canvas = (PSwingCanvas) getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        JTextField tf = new JTextField("<edit me>");
        PSwing ps = new PSwing(tf);
        layer.addChild(ps);

        PPath r1 = PPath.createRectangle(0, 0, 50, 50);
        r1.setPaint(Color.RED);
        PPath r2 = PPath.createRectangle(25, 25, 50, 50);
        r2.setPaint(Color.GREEN);
        layer.addChild(r2);
        layer.addChild(r1);

        ps.addInputEventListener(new PDragEventHandler());
        //canvas.removeInputEventListener(canvas.getPanEventHandler());

        camera.addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                System.out.println(e);
            }
        });
    }

}