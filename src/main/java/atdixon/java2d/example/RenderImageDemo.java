package atdixon.java2d.example;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RenderImageDemo extends JComponent {

    private static final int DIAMETER = 150;
    private static final int COLS = 4;
    private static final int ROW_HEIGHT = 225;
    private static final int CELL_MARGIN = 25;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new RenderImageDemo());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private final List<BufferedImage> images = new ArrayList<BufferedImage>();

    private RenderImageDemo() {
        setOpaque(true);
        setDoubleBuffered(true);
        // base
        images.add(createImage(DIAMETER, 1, 0, false));
        images.add(createImage(DIAMETER, 1, 0, true));
        images.add(createImage(DIAMETER, 20, 0, false));
        images.add(createImage(DIAMETER, 20, 0, true));
        // odd diameter
        images.add(createImage(DIAMETER + 1, 1, 0, false));
        images.add(createImage(DIAMETER + 1, 1, 0, true));
        images.add(createImage(DIAMETER + 1, 20, 0, false));
        images.add(createImage(DIAMETER + 1, 20, 0, true));
        // non-int path offset
        images.add(createImage(DIAMETER, 1, 0.2f, false));
        images.add(createImage(DIAMETER, 1, 0.2f, true));
        images.add(createImage(DIAMETER, 20, 0.2f, false));
        images.add(createImage(DIAMETER, 20, 0.2f, true));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1024, rows() * (ROW_HEIGHT + CELL_MARGIN));
    }

    private int rows() {
        return (int) Math.ceil(images.size() / (float) COLS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());
        final int rows = rows();
        for (int r = 0; r < rows; ++r) {
            int offset = CELL_MARGIN;
            for (int i = 0; i < 4; ++i) {
                BufferedImage img = images.get(r * COLS + i);
                g2.drawImage(img, null, offset, r * ROW_HEIGHT + CELL_MARGIN);
                offset += img.getWidth() + CELL_MARGIN;
            }
        }
    }

    private static BufferedImage createImage(int diameter, float strokeWidth, float pathOffset, boolean highQuality) {
        final GraphicsConfiguration gConf = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice().getDefaultConfiguration();

        BasicStroke stroke = new BasicStroke(strokeWidth);

        GeneralPath path = new GeneralPath();
        Ellipse2D.Float shape = new Ellipse2D.Float(pathOffset, pathOffset, diameter, diameter);
        path.append(shape, false);

        Rectangle2D b = stroke.createStrokedShape(path).getBounds2D();

        BufferedImage img =
                gConf.createCompatibleImage(
                        (int) Math.ceil(b.getWidth()),
                        (int) Math.ceil(b.getHeight()), Transparency.TRANSLUCENT);
        Graphics2D g2 = img.createGraphics();

        if (highQuality) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }

        g2.setPaint(Color.green);
        g2.fillRect(0, 0, img.getWidth(), img.getHeight());

        double tx = b.getX(); // Math.floor(b.getX());
        double ty = b.getY(); // Math.floor(b.getY());

        g2.translate(-tx, -ty);

        g2.setPaint(Color.black);
        g2.setStroke(stroke);

        g2.draw(path);
        g2.dispose();
        return img;
    }

}
