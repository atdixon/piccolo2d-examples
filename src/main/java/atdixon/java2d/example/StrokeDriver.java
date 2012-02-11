package atdixon.java2d.example;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;

public class StrokeDriver extends PFrame {

    public static void main(String[] args) {
        new StrokeDriver();
    }

    @Override
    public void initialize() {
        PLayer layer = getCanvas().getLayer();
        PPath path = PPath.createEllipse(0, 0, 100, 100);
        path.setStroke(new TextStroke("Aaron Dixon", new Font("Helvetica", Font.PLAIN, 12)));
        layer.addChild(path);
    }

}
