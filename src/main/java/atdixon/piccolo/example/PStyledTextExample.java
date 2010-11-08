package atdixon.piccolo.example;

import atdixon.piccolo.support.CustomBreakingText;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.handles.PBoundsHandle;

import javax.swing.text.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PStyledTextExample extends PFrame {

    private static final String PKG = "atdixon.piccolo.example";
    private static final String CLS = "\nPStyledTextExample";

    public static void main(String[] args) {
        new PStyledTextExample();
    }

    @Override
    public void initialize() {
        setSize(800, 600);
        PCanvas cnv = getCanvas();
        PLayer lyr = cnv.getLayer();

        final PPath sqr = PPath.createRectangle(0, 0, 300, 100);
        sqr.setStrokePaint(Color.green);
        PBoundsHandle.addBoundsHandlesTo(sqr);

        final CustomBreakingText text = new CustomBreakingText() {
            @Override
            public boolean isBreakableChar(char c) {
                return Character.isUpperCase(c) || Character.isSpaceChar(c);
            }
        };
        text.setInsets(new Insets(10,10,10,10));
        text.setConstrainWidthToTextWidth(false);
        text.setDocument(document());

        sqr.addChild(text);

        sqr.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                double yoff = (sqr.getHeight() - text.getHeight()) / 2;
                text.setBounds(0, yoff, sqr.getWidth(), text.getHeight());
            }
        });

        sqr.offset(100, 100);

        lyr.addChild(sqr);
    }

    private Document document() {
        Document document = new DefaultStyledDocument();
        try {
            document.insertString(0, PKG, attrs(12));
            document.insertString(PKG.length(), CLS, attrs(16));
        }
        catch (BadLocationException e) {
            // ignore
        }
        return document;
    }

    private SimpleAttributeSet attrs(int fontSize) {
        SimpleAttributeSet as = new SimpleAttributeSet();
        as.addAttribute(StyleConstants.CharacterConstants.FontFamily, "Helvetica");
        as.addAttribute(StyleConstants.CharacterConstants.FontSize, fontSize);
        as.addAttribute(StyleConstants.ALIGN_RIGHT, true);
        return as;
    }

}
