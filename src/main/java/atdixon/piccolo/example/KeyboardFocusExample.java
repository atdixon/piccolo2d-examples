package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.PFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

public class KeyboardFocusExample extends PFrame {

    public static void main(String[] args) {
        new KeyboardFocusExample();
    }

    @Override
    public void initialize() {
        setSize(1200, 600);
        invalidate();

        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        final TextNode node = new TextNode();
        node.setOffset(canvas.getBounds().getCenterX(), canvas.getBounds().getCenterY());
        layer.addChild(node);

        node.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseEntered(PInputEvent e) {
                node.setStrokePaint(Color.red);
                e.getInputManager().setKeyboardFocus(e.getPath());
            }
            @Override
            public void mouseExited(PInputEvent e) {
                node.setStrokePaint(Color.blue);
                e.getInputManager().setKeyboardFocus(null);
            }
            @Override
            public void keyPressed(PInputEvent e) {
                node.text(String.valueOf(e.getKeyChar()));
            }
        });
    }

    /** Text node. */
    private static final class TextNode extends PPath {

        private final PText text = new PText();

        TextNode() {
            super(new Rectangle2D.Float(0, 0, 50, 50));
            setStrokePaint(Color.blue);
            setPaint(Color.white);
            setChildrenPickable(false);
            text.setFont(new Font("Tahoma", Font.BOLD, 24));
            addChild(text);
            text("A");
        }

        void text(String text) {
            this.text.setText(text);
            centerText();
        }

        private void centerText() {
            text.setOffset((getWidth() - text.getWidth()) / 2, (getHeight() - text.getHeight()) / 2);
        }

    }

}