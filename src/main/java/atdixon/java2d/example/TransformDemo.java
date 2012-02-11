package atdixon.java2d.example;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class TransformDemo {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JComponent canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static final class Canvas extends JComponent {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            System.out.println(g2.getTransform());

            g2.setColor(Color.white);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.green);
            g2.drawRect(10, 10, getWidth() - 20, getHeight() - 20);

            g2.setColor(Color.blue);
            g2.drawRect(20, 20, 50, 50);
            g2.drawOval(-10, -10, 20, 20);

            // scale, and draw
            g2.setColor(Color.red);
            AffineTransform x2 = new AffineTransform(2, 0, 0, 2, 0, 0);
            g2.transform(x2);
            g2.drawRect(20, 20, 50, 50);
            g2.drawOval(-10, -10, 20, 20);

            System.out.println(g2.getTransform());

            invert(x2);
            g2.transform(x2);

            System.out.println(g2.getTransform());

            // translate to center of blue, scale, and draw
            g2.translate(-45, -45);
            g2.scale(2, 2);
            g2.setColor(Color.magenta);
            g2.drawRect(20, 20, 50, 50);

            System.out.println(g2.getTransform());

            g2.scale(.5, .5);
            g2.translate(45, 45);

            System.out.println(g2.getTransform());

            g2.transform(new AffineTransform(2, 0, 0, 2, 0, 0));
            g2.transform(new AffineTransform(2, 0, 0, 2, 0, 0));

            System.out.println(g2.getTransform());
        }

    }

    private static void invert(AffineTransform x2) {
        try {
            x2.invert();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
    }

}