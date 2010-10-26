package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.handles.PBoundsHandle;
import edu.umd.cs.piccolox.util.PBoundsLocator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A few ways to keep bounds handles unaffected by zooms/camera view transforms.
 */
public class BoundsHandleExample extends PFrame {

    public static void main(String[] args) {
        new BoundsHandleExample();
    }

    @Override
    public void initialize() {
        final PCamera camera = getCanvas().getCamera();
        final PLayer layer = getCanvas().getLayer();

        final PPath node = PPath.createRectangle(20, 20, 200, 50);

        layer.addChild(node);

        final PBoundsHandle handle = new PBoundsHandle(PBoundsLocator.createSouthEastLocator(node));
        node.addChild(handle);

        camera.addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                handle.setScale(1 / camera.getViewScale());
            }
        });

    }

}
