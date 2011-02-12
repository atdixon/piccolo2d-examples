package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ToImageExample extends PFrame {

    public static void main(String[] args) {
        new ToImageExample();
    }

    private static final float STROKE_WIDTH = 5f;

    @Override
    public void initialize() {
        PCanvas canvas = getCanvas();
        PLayer layer = canvas.getLayer();

        PPath circle = new PPath(new Ellipse2D.Double(0, 0, 100, 100 ));
        circle.setPaint(Color.RED);
        circle.setStroke(new BasicStroke(STROKE_WIDTH));

        PImage image = new PImage(circle.toImage());
        image.setOffset(10, 10);

        layer.addChild(image);

        // show actual rendered image in a non-piccolo context
        JFrame popup = new JFrame();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(toImageBytes(circle)));
        popup.getContentPane().add(label);
        popup.pack();
        popup.setVisible(true);
    }

    private byte[] toImageBytes(PPath circle) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) circle.toImage(), "PNG", out);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return out.toByteArray();
    }

}