package atdixon.piccolo.example;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.LayoutManager;

/**
 * Appears that piccolo doesn't support PSwing nodes on the camera (ie sticky nodes).
 * This is a simple demo of having a navigation swing widget layered over a PCanvas using
 * standard swing capabilities.
 */
public class StickyNavigationExample extends JFrame {

    private static final Dimension SIZE = new Dimension(800, 600);
    private static final int TOOL_WIDTH = 200;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StickyNavigationExample();
            }
        });
    }

    private final PSwingCanvas canvas;
    private final JScrollPane tree;

    public StickyNavigationExample() throws HeadlessException {
        Container pane = getContentPane();
        pane.add(tree = tree());
        pane.add(canvas = canvas());
        pane.setLayout(new InternalLayout());
        pack();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private PSwingCanvas canvas() {
        PSwingCanvas canvas = new PSwingCanvas();
        canvas.getLayer().addChild(PPath.createRectangle(0,0,100,100));
        return canvas;
    }

    private JScrollPane tree() {
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        scroll.setViewportView(new JTree());
        return scroll;
    }

    /** InternalLayout. */
    private class InternalLayout implements LayoutManager {

        public void addLayoutComponent(String name, Component comp) {
            throw new IllegalStateException("unexpected");
        }

        public void removeLayoutComponent(Component comp) {
            throw new IllegalStateException("unexpected");
        }

        public Dimension preferredLayoutSize(Container parent) {
            return SIZE;
        }

        public Dimension minimumLayoutSize(Container parent) {
            return SIZE;
        }

        public void layoutContainer(Container parent) {
            tree.setBounds(0, 0, TOOL_WIDTH, 600);
            canvas.setBounds(TOOL_WIDTH, 0, (int) (SIZE.getWidth() - TOOL_WIDTH), 600);
        }
    }


}
