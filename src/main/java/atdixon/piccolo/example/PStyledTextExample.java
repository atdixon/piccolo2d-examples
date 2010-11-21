package atdixon.piccolo.example;

import atdixon.piccolo.example.support.CustomBreakingText;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.handles.PBoundsHandle;

import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PStyledTextExample extends PFrame {

    private static final String PRE = "example : ";
    private static final String PKG = "atdixon.piccolo.example";
    private static final String CLS = "\nPStyledTextExample";

    public static void main(String[] args) {
        new PStyledTextExample();
    }

    private PPath square;
    private CustomBreakingText text;

    @Override
    public void initialize() {
        setSize(800, 600);
        PCanvas cnv = getCanvas();
        PLayer lyr = cnv.getLayer();

        square = PPath.createRectangle(0, 0, 300, 100);
        square.setStrokePaint(Color.green);
        PBoundsHandle.addBoundsHandlesTo(square);

        text = new CustomBreakingText() {
            @Override
            public boolean isBreakableChar(char c) {
                return Character.isUpperCase(c) || Character.isSpaceChar(c);
            }
        };
        text.setDocument(document());
        text.setInsets(new Insets(10,10,10,10));
        text.setConstrainWidthToTextWidth(false);
        text.setPickable(false);
        
        square.addChild(text);

        // scale, just so we can see that our parent/child positioning translations are correct
        text.scale(1.2);

        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateTextPosition();
            }
        };
        square.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, l);

        lyr.addChild(square);
        square.offset(100, 100);
        updateTextPosition();
    }

    private void updateTextPosition() {
        Rectangle2D p2l = text.parentToLocal(square.getBounds());
        Rectangle2D l2p = text.localToParent(text.getBounds());
        double yoff = (square.getHeight() - l2p.getHeight()) / 2;
        text.setOffset(square.getX(), square.getY() + yoff);
        text.setWidth(p2l.getWidth());
    }

    private Document document() {
        Document document = new DefaultStyledDocument();
        try {
            document.insertString(0, PRE, attrs(8, true));
            document.insertString(document.getLength(), PKG, attrs(12, false));
            document.insertString(document.getLength(), CLS, attrs(16, false));
        }
        catch (BadLocationException e) {
            // ignore
        }
        return document;
    }

    private SimpleAttributeSet attrs(int fontSize, boolean italic) {
        SimpleAttributeSet as = new SimpleAttributeSet();
        as.addAttribute(StyleConstants.CharacterConstants.FontFamily, "Helvetica");
        as.addAttribute(StyleConstants.CharacterConstants.FontSize, fontSize);
        as.addAttribute(StyleConstants.CharacterConstants.Italic, italic);
        as.addAttribute(StyleConstants.ALIGN_RIGHT, true);
        return as;
    }

}
