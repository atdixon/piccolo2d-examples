package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolox.PFrame;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

/**
 * Demonstrates having some nodes of a constant size. In this example, circles keep their size
 * regardless of view scale. These circles also maintain center position within a square.
 */
public class ConstantScaleNodeExample2 extends PFrame {

    private static final Random RANDOM = new Random();
    private static final int NUM_BLOCKS = 100;

    public static void main(String[] args) {
        new ConstantScaleNodeExample2();
    }

    @Override
    public void initialize() {
        setSize(800, 600);
        final PCanvas canvas = getCanvas();
        canvas.setBackground(Color.black);
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        for (int i = 0; i < NUM_BLOCKS; ++i) {
            final Block block = new Block();
            block.setOffset(randomPoint());
            layer.addChild(block);
            final Circle circle = new Circle();
            block.addChild(circle);
            circle.centerBoundsOnPoint(block.getBounds().getCenterX(), block.getBounds().getCenterY());
            camera.addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    circle.setScale(1 / camera.getViewScale());
                    final PBounds anchor = block.getBounds();
                    circle.centerFullBoundsOnPoint(anchor.getCenterX(), anchor.getCenterY());
                }
            });
        }
    }

    /** Square. */
    private static final class Block extends PPath {

        Block() {
            setPaint(randomColor());
            setStrokePaint(Color.white);
            setPathToRectangle(0, 0, 50, 50);
        }

    }

    /** Circle. Must be added as a child of a {@link edu.umd.cs.piccolo.PCamera}. */
    private static final class Circle extends PPath {
        
        Circle() {
            setPaint(Color.white);
            setStrokePaint(Color.black);
            setPathToEllipse(0, 0, 20, 20);
        }

    }

    private static Color randomColor() {
        return new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255));
    }

    private static Point randomPoint() {
        return new Point(RANDOM.nextInt(1000), RANDOM.nextInt(1000));
    }

}