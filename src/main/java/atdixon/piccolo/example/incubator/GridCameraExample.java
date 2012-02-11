package atdixon.piccolo.example.incubator;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PRoot;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.PFrame;

import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class GridCameraExample extends PFrame {

    private static final Color COLOR = Color.blue;
    private static final Color LIGHT_COLOR = new Color(0xaaaaff);

    private static final int SPACING = 25;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GridCameraExample(new PCanvas() {
                    @Override
                    protected PCamera createDefaultCamera() {
                        final PRoot root = new PRoot();
                        final PLayer layer = new PLayer();
                        final PCamera camera = new GridCamera();
                        root.addChild(camera);
                        root.addChild(layer);
                        camera.addLayer(layer);
                        return camera;
                    }
                });
            }
        });
    }

    GridCameraExample(PCanvas canvas) {
        super(GridCameraExample.class.getSimpleName(), false, canvas);
    }

    /**
     * A camera that paints a grid before painting its layers.
     */
    private static final class GridCamera extends PCamera {

        private static final AffineTransform IDENTITY = new AffineTransform();

        @Override
        protected void paintCameraView(PPaintContext pc) {
            final double h = getBoundsReference().getHeight();
            final double w = getBoundsReference().getWidth();

            final Graphics2D g = pc.getGraphics();
            final Graphics2D g_prime = (Graphics2D) g.create();

            if (pc.getScale() > 0.1) {
                double[] m = new double[6];
                g.getTransform().getMatrix(m);

                g_prime.setTransform(IDENTITY);

                double S = m[3] * SPACING;

                Stroke sx = new BasicStroke((float) m[0]);
                double nx = m[4] / S;
                double fx = nx - (long) nx;
                double dx = (1 - fx) * S;

                Stroke sy = new BasicStroke((float) m[3]);
                double ny = m[5] / S;
                double fy = ny - (long) ny;
                double dy = (1 - fy) * S;

                g_prime.setColor(COLOR);

                g_prime.setStroke(sx);
                for (double x_i = -dx; x_i < w; x_i += S) {
                    g_prime.drawLine((int) x_i, 0, (int) x_i, (int) h);
                }

                g_prime.setStroke(sy);
                for (double y_i = -dy; y_i < h; y_i += S) {
                    g_prime.drawLine(0, (int) y_i, (int) w, (int) y_i);
                }
            } else {
                // our view scale is too small, just paint a light background
                g_prime.setColor(LIGHT_COLOR);
                g_prime.setTransform(IDENTITY);
                g_prime.fillRect(0, 0, (int) getWidth(), (int) getHeight());
            }

            g_prime.dispose();

            super.paintCameraView(pc);
        }

    }
}