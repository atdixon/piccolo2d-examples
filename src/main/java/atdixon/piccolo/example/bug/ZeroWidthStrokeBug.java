package atdixon.piccolo.example.bug;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;

public class ZeroWidthStrokeBug extends PFrame {

    public static void main(String[] args) {
        new ZeroWidthStrokeBug();
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        PPath line = PPath.createLine(0, 0, canvas.getWidth(), canvas.getHeight());
        line.setStroke(new BasicStroke(0));

        layer.addChild(line);

    }

}