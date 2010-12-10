package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class RectangleClickExample extends PFrame {

    public static void main(String[] args) {
        new RectangleClickExample();
    }

    static class Rectangle extends PPath {

        Rectangle() {
            setPathToRectangle(0, 0, 100, 100);
        }

        @Override
        public boolean intersects(Rectangle2D b) {
            return getBoundsReference().intersects(b);
        }
    }

    @Override
    public void initialize() {

        final PPath rect = new Rectangle();

        rect.setStroke(new BasicStroke(5));

        getCanvas().getLayer().addChild(rect);

        rect.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent event) {
                rect.setStrokePaint(other(rect.getStrokePaint()));
            }
        });
    }

    private Paint other(Paint current) {
        return current == Color.green ? Color.blue : Color.green;
    }

}
