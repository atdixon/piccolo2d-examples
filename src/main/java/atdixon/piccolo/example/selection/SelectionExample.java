package atdixon.piccolo.example.selection;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.PFrame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

/**
 * Supports:
 *      Sticky (marquee doesn't disappear after drag) selection
 *      Esc-to-clear-selection
 *      Right+click zooming
 */
public class SelectionExample extends PFrame {

    public static void main(String[] args) {
        new SelectionExample();
    }

    private static final Random RANDOM = new Random();

    @Override
    public void initialize() {
        setSize(new Dimension(800, 600));
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        // remove panning, keep zooming
        getCanvas().removeInputEventListener(getCanvas().getPanEventHandler());

        final SimpleSelectionHandler selectionHandler = new SimpleSelectionHandler();
        getCanvas().addInputEventListener(selectionHandler);
        getCanvas().getRoot().getDefaultInputManager().setKeyboardFocus(new PBasicInputEventHandler() {
            @Override
            public void keyPressed(PInputEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    selectionHandler.clearMarqueeSelection();
                }
            }
        });

        camera.addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                // if the camera view changes (e.g., pan/zoom), clear the marquee
                selectionHandler.clearMarqueeSelection();
            }
        });

        for (int i = 0; i < 100; ++i) {
            final Box box = new Box(0, 0, 50, 50);
            box.setOffset(RANDOM.nextInt(800), RANDOM.nextInt(600));
            layer.addChild(box);
        }
    }

    private static final class Tag extends PPath implements IHighlightable {

        private static final Color DEFAULT_PAINT = Color.white;
        private static final Color HIGHLIGHTED = Color.pink;

        Tag(String value) {
            final PText text = new PText(value);
            double dim = Math.max(text.getWidth() + 10, text.getHeight() + 10);
            setPathTo(new Ellipse2D.Double(0, 0, dim, dim));
            text.centerFullBoundsOnPoint(getX() + getWidth() / 2, getY() + getHeight() / 2);
            addChild(text);
            setPaint(DEFAULT_PAINT);
        }

        @Override
        public void highlight() {
            setPaint(HIGHLIGHTED);
        }

        @Override
        public void unhighlight() {
            setPaint(DEFAULT_PAINT);
        }

    }

    /** Custom node. */
    private static final class Box extends PPath implements ISelectable, IHighlightable {

        private static char letters = 'A';

        private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1f);
        private static final Stroke SELECTED = new BasicStroke(5f);
        private static final Color DEFAULT_PAINT = Color.white;
        private static final Color HIGHLIGHTED = Color.pink;

        Box(float x, float y, float w, float h) {
            super(new Rectangle2D.Float(x, y, w, h));
            setPaint(DEFAULT_PAINT);
            setStroke(DEFAULT_STROKE);
            Tag tag = new Tag(String.valueOf((char) ((letters++ - 'A') % ('Z' - 'A') + 'A')));
            tag.centerFullBoundsOnPoint(getX() + getWidth() / 2, getY() + getHeight() / 2);
            addChild(tag);
        }

        @Override
        public void select() {
            setStroke(SELECTED);
        }

        @Override
        public void deselect() {
            setStroke(DEFAULT_STROKE);
        }

        @Override
        public void highlight() {
            setPaint(HIGHLIGHTED);
        }

        @Override
        public void unhighlight() {
            setPaint(DEFAULT_PAINT);
        }

    }

}