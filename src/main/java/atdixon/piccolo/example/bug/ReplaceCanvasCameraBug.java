package atdixon.piccolo.example.bug;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.event.PZoomEventHandler;
import edu.umd.cs.piccolo.util.PUtil;

import javax.swing.*;
import java.awt.*;

public class ReplaceCanvasCameraBug extends JFrame {

    private static final Dimension MIN_SIZE = new Dimension(450, 300);

    private JComponent panel;
    private PCanvas sg;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ReplaceCanvasCameraBug();
            }
        });
    }
    
    public ReplaceCanvasCameraBug() {
        getLayeredPane().setLayout(new InternalLayout());
        getLayeredPane().add(panel = panel());
        getLayeredPane().add(sg = scenegraph());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        PCamera camera = PUtil.createBasicScenegraph();
        sg.setCamera(camera);
        drawOn(camera.getLayer(0));
        sg.setPanEventHandler(new PPanEventHandler());
        sg.setZoomEventHandler(new PZoomEventHandler());
    }

    private static JPanel panel() {
        JPanel jp = new JPanel();
        jp.setBackground(Color.blue);
        jp.setOpaque(true);
        jp.setPreferredSize(new Dimension(100, 100));
        return jp;
    }

    private static PCanvas scenegraph() {
        PCanvas sg = new PCanvas();
        sg.setBackground(Color.yellow);
        sg.setOpaque(true);
        return sg;
    }

    private static void drawOn(PLayer layer) {
        PNode n = new PNode();
        n.setBounds(0, 0, 100, 100);
        n.offset(200, 200);
        n.setPaint(Color.green);
        layer.addChild(n);
    }

    /** Specialized layout for Heatlamp layers. */
    private class InternalLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeLayoutComponent(Component c) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            // we don't expect the top-level HeatLamp panel to be hosted anywhere but in the
            // top-level frame, so what we return here is moot
            return MIN_SIZE;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return MIN_SIZE;
        }

        @Override
        public void layoutContainer(Container parent) {
            // the contentPane contains the dimensions of the JFrame - frame insets
            final Container contentPane = ReplaceCanvasCameraBug.this.getContentPane();

            panel.setBounds(0, 0, contentPane.getWidth(), 30);
            sg.setBounds(0, 30, contentPane.getWidth(), contentPane.getHeight() - 30);
        }
    }

}