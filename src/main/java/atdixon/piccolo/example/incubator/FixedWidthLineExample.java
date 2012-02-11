package atdixon.piccolo.example.incubator;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.BasicStroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FixedWidthLineExample extends PFrame {

    public static void main(String[] args) {
        new FixedWidthLineExample();
    }

    private PCamera camera;

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();
        camera = canvas.getCamera();

        final PPath fixed = PPath.createLine(0, 0, 500, 500);
        final PPath unfixed = PPath.createLine(20, 0, 520, 500);

        layer.addChild(fixed);
        layer.addChild(unfixed);

        camera.addPropertyChangeListener("viewTransform", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                fixed.setStroke(new BasicStroke((float) (1d / camera.getViewScale())));
            }
        });
    }

}