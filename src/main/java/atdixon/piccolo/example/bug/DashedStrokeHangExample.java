package atdixon.piccolo.example.bug;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;

public class DashedStrokeHangExample extends PFrame {

    private static final float[] strokeDash = { 100f, 5f };
    private static final Stroke dashed = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f, strokeDash, 0.0f);

    public static void main(String[] args) {
        new DashedStrokeHangExample();
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        PPath path = new PPath();
        path.setStroke(dashed);
        path.setStrokePaint(Color.lightGray);
        //[-0.5, 30.0, -8399.0, 30.0]
        path.moveTo(-0.5f, 30);
        path.lineTo(-1183900, 30);
        layer.addChild(path);

    }

}