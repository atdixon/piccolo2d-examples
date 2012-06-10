package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.PFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

public class ZoomGraphExample extends PFrame {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        new ZoomGraphExample();
    }

    @Override
    public void initialize() {
        setSize(WIDTH, HEIGHT);
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        for (String city : CITIES) {
            final GraphNode node = new GraphNode(city);
            node.setOffset(randomPoint());
            node.observe(camera);
            layer.addChild(node);
        }
    }

    private static Color randomColor() {
        return new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255));
    }

    private static Point randomPoint() {
        final int xf = RANDOM.nextInt(2) == 0 ? -1 : 1;
        final int yf = RANDOM.nextInt(2) == 0 ? -1 : 1;
        return new Point(xf * RANDOM.nextInt(WIDTH * 2), yf * RANDOM.nextInt(HEIGHT * 2));
    }

    private static class GraphNode extends PNode {

        private final PPath circle;
        private final PText label;

        GraphNode(String title) {
            circle = PPath.createEllipse(0, 0, 60, 60);
            circle.setPaint(randomColor());
            circle.setStrokePaint(null);
            addChild(circle);
            label = new PText(title);
            label.setFont(label.getFont()
                .deriveFont(Font.BOLD).deriveFont(16f));
            label.setTextPaint(Color.white);
            label.setPaint(new Color(0, 0, 0, .5f));
            addChild(label);
        }

        public void observe(final PCamera camera) {
            camera.addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    label.setScale(1 / camera.getViewScale());
                }
            });
        }

        @Override
        protected void layoutChildren() {
            circle.centerBoundsOnPoint(0, 0);
            label.centerBoundsOnPoint(0, 0);
        }

    }

    private static final String[] CITIES = new String[] {
        "Shanghai", "Karachi", "Mumbai", "Delhi", "Ahmedabad",
        "Istanbul", "São Paulo", "Moscow", "Seoul", "Beijing",
        "Mexico City", "Tokyo", "Kinshasa", "Jakarta",
        "New York City", "Lima", "London", "Bogotá", "Tehran",
        "Ho Chi Minh City", "Hanoi", "Hong Kong", "Bangkok",
        "Dhaka", "Cairo", "Lahore", "Rio de Jeneiro", "Chongqing",
        "Chennai", "Bangalore", "Tianjin", "Baghdad", "Kolkata",
        "Singapore", "Riyadh", "Saint Petersberg", "Surat", "Yangon",
        "Guangzhou", "Shenzhen", "Chengdu", "Harbin", "Qingdao",
        "Changchun", "Xi'an", "Nanjing", "Shenyang", "Dalian",
        "Jinan", "Dongguan", "Zhanjiang", "Moaming", "Shijiazhuang",
        "Shantou", "Foshan", "Meizhou", "Huizhou", "Jiangmen", "Hangzhou",
        "Ningbo", "Wenzhou", "Jiaxing", "Jinhua", "Shaoxing", "Taizhou",
        "Alexandria", "Shenyang", "Hyderabad", "Ankara", "Johannesburg",
        "Los Angeles", "Wuhan", "Yokahoma", "Abidjan", "Busan", "Cape Town",
        "Durban", "Pune", "Berlin", "Pyongyang", "Jeddah", "Kanpur", "Madrid",
        "Jaipur", "Buenos Aires", "Nairobi"
    };

}