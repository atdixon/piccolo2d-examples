package atdixon.piccolo.example.support;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

import java.awt.geom.Point2D;

public class ScrollZoomEventHandler extends PBasicInputEventHandler {

    private static final double SCALE_FACTOR = 1.1;

    @Override
    public void mouseWheelRotated(PInputEvent e) {
        super.mouseWheelRotated(e);
        dump("wheel", e);
        final PCamera camera = e.getCamera();
        final Point2D canvasPosition = e.getCanvasPosition();
        camera.localToView(canvasPosition); // apply inverse of view transform before we scaleViewAboutPoint
        final int wheelRotation = e.getWheelRotation();
        final double scaleFactor = wheelRotation < 0 ? 1 / SCALE_FACTOR : SCALE_FACTOR;
        camera.scaleViewAboutPoint(Math.pow(scaleFactor, Math.abs(wheelRotation)), canvasPosition.getX(), canvasPosition.getY());
    }

    @Override
    public void mouseWheelRotatedByBlock(PInputEvent e) {
        super.mouseWheelRotatedByBlock(e);
        dump("block", e);
    }

    private static void dump(String head, PInputEvent e) {
        System.out.println("=== " + head + " ===");
        System.out.println("\twheel rotation = " + e.getWheelRotation());
        System.out.println("\tcanvas position = " + e.getCanvasPosition());
    }

}
