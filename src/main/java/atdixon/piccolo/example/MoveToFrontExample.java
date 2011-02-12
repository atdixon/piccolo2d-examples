package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

public class MoveToFrontExample extends PFrame {

    public static void main(String[] args) {
        new MoveToFrontExample();
    }

    @Override
    public void initialize() {
        PCanvas canvas = getCanvas();
        PLayer layer = canvas.getLayer();

        PPath c1 = PPath.createRectangle(0, 0, 100, 50);
        layer.addChild(c1);

        PPath c2 = PPath.createRectangle(0, 0, 100, 50);
        layer.addChild(c2);

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        PDragEventHandler drag = new PDragEventHandler();
        drag.setMoveToFrontOnPress(true);
        canvas.addInputEventListener(drag);

    }

}