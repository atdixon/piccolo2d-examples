package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.event.PZoomEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.event.InputEvent;

/**
 * Demonstrates pan and zooming with a right-click; when shift is pressed, right-click pans, else right-
 * click zooms.
 */
public class CustomPanAndZoomExample extends PFrame {

    public static void main(String[] args) {
        new CustomPanAndZoomExample();
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        layer.addChild(PPath.createRectangle(0, 0, 100, 100));

        canvas.setPanEventHandler(new CustomPanHandler());
        canvas.setZoomEventHandler(new CustomZoomHandler());
    }

    private static class CustomZoomHandler extends PZoomEventHandler {

        @Override
        public boolean acceptsEvent(PInputEvent event, int type) {
            return super.acceptsEvent(event, type) && !event.isShiftDown();
        }

    }

    private static class CustomPanHandler extends PPanEventHandler {

        public CustomPanHandler() {
            setEventFilter(new PInputEventFilter(InputEvent.BUTTON3_MASK));
        }

        @Override
        public boolean acceptsEvent(PInputEvent event, int type) {
            return super.acceptsEvent(event, type) && event.isShiftDown();
        }

    }

}