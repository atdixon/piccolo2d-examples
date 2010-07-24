package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.activities.PTransformActivity;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.PFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This is to demonstrate memory consumption of PImage nodes in a Piccolo2D application.
 * The example allows navigation across an 'infinite' grid of PImages, pan-animating the view
 * to different pages based on user input.
 *
 * Note that when you run this example, you can click on the status text to switch between
 * an optimized implementation (in which we free PImages that leave the current view) or a
 * non-optimized behavior.
 */
public class PImageMemoryExample extends PFrame {

    /**
     * Establish a base url that can generate dynamic images; one such generator is dummyimage.com,
     * however that site generates lightweight gifs so we'll use a site that will produce more
     * memory-consuming images.
     */
    private static final String IMAGE_BASE_URL = "http://www.hetemeel.com/einsteinshow.php?text=";
    //private static final String IMAGE_BASE_URL = "http://dummyimage.com/100x100/000/fff&text=";

    private static final int PAGE_WIDTH_IMAGES = 5;
    private static final int IMG_SIZE = 100;
    private static final int PAGE_SIZE = PAGE_WIDTH_IMAGES * IMG_SIZE;

    private PLayer layer;

    /**
     * A text node that we'll use to show status, like free memory.
     */
    private PText status;

    /**
     * True if we are freeing up image memory as the user navigates; togglable through the UI.
     */
    private boolean optimized = true;

    /**
     * Keep track of PImages that we've already added to the view; once a PImage is added to our
     * layer, we never remove it.
     */
    private Map<Point, PImage> nodes = new HashMap<Point, PImage>();

    /**
     * Reuse java.awt.Point class to maintain a "page coordinate" representing the currently
     * viewed page.
     */
    private Point currentPage = new Point(0, 0);

    public static void main(String[] args) {
        new PImageMemoryExample();
    }

    @Override
    public void initialize() {
        setSize(PAGE_SIZE, PAGE_SIZE);
        setResizable(false);
        layer = getCanvas().getLayer();
        loadOrUnloadImagesOnPage(currentPage, true);
        status = createStatusNode();
        getCanvas().getCamera().addChild(status);
        getCanvas().removeInputEventListener(getCanvas().getPanEventHandler());
        getCanvas().removeInputEventListener(getCanvas().getZoomEventHandler());
        // Create an input handler that lets us pan around the page with up, down, left and right keys.
        getCanvas().getRoot().getDefaultInputManager().setKeyboardFocus(new PBasicInputEventHandler() {
            @Override
            public void keyPressed(PInputEvent event) {
                switch (event.getKeyCode()) {
                    case 37: moveToPage(translated(currentPage, -1, 0)); break; // left
                    case 38: moveToPage(translated(currentPage, 0, -1)); break; // up
                    case 39: moveToPage(translated(currentPage, 1, 0)); break; // right
                    case 40: moveToPage(translated(currentPage, 0, 1)); break; // down
                }
            }
        });
        // Create a timer that can update our memory status.
        new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStatus();
            }
        }).start();
    }

    private void updateStatus() {
        status.setText((optimized ? "optimized" : "not optimized") +
            ", free = " + (Runtime.getRuntime().freeMemory() / 1000) + "K");
    }

    private PText createStatusNode() {
        PText text = new PText();
        text.setPaint(Color.white);
        text.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent event) {
                if (event.getClickCount() == 2) {
                    optimized = !optimized;
                    updateStatus();
                }
            }
        });
        return text;
    }

    private void moveToPage(Point page) {
        loadOrUnloadImagesOnPage(page, true);
        int x = page.x * PAGE_WIDTH_IMAGES * IMG_SIZE,
            y = page.y * PAGE_WIDTH_IMAGES * IMG_SIZE;
        Rectangle2D bounds = new Rectangle2D.Double(x, y, PAGE_SIZE, PAGE_SIZE);
        PTransformActivity pan = getCanvas().getCamera().animateViewToPanToBounds(bounds, 1000);
        // Only unload images on the current page after we've completed the pan to the new page.
        unloadImagesAfter(currentPage, pan);
        currentPage = page;
    }

    private void unloadImagesAfter(final Point page, PActivity activity) {
        PActivity unload = new PActivity(-1) {
            @Override
            protected void activityStep(long elapsedTime) {
                loadOrUnloadImagesOnPage(page, false);
                terminate(); // only run once
            }
        };
        if (activity != null) {
            unload.startAfter(activity);
        }
        getCanvas().getRoot().addActivity(unload);
    }

    /**
     * Load or unload images on the provided page based on the provided load flat (false = unload).
     * This method updates the UI and should be invoked from the UI thread only.
     */
    private void loadOrUnloadImagesOnPage(Point page, boolean load) {
        int row = (int) page.getY() * PAGE_WIDTH_IMAGES,
            col = (int) page.getX() * PAGE_WIDTH_IMAGES;
        for (int i = row; i < row + 5; ++i) {
            for (int j = col; j < col + 5; ++j) {
                final PImage node = getImageNodeAt(new Point(j, i));
                if (load) {
                    final URL url = (URL) node.getAttribute("url");
                    // We are in the UI thread, so do not load the image in this thread;
                    // create a thread to load the image asynchronously.
                    new Thread() {
                        @Override
                        public void run() {
                            // Here, we leverage the swing ImageIcon constructor which uses the
                            // Toolkit#getImage() api to load the image and blocks until the image
                            // is loaded.
                            final ImageIcon icon = new ImageIcon(url);
                            // ImageIcon has loaded the image, so NOW we can update the node in the UI
                            // thread.
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    // Note that Piccolo2d uses ImageIcon under the covers to load the image
                                    // when we call setImage here. This should be a quick operation as we've
                                    // previously loaded the image, so now we can do it in the UI thread.
                                    node.setImage(icon.getImage());
                                    node.setBounds(0, 0, 100, 100);
                                }
                            });
                        }
                    }.start();
                } else {
                    if (optimized) {
                        freeImage(node);
                    }
                }
            }
        }
    }

    private void freeImage(PImage node) {
        final Image toFlush = node.getImage();
        node.setImage((Image) null);
        new Thread() {
            @Override
            public void run() {
                if (toFlush != null) {
                    toFlush.flush();
                }
            }
        }.start();
    }

    private PImage getImageNodeAt(Point loc) {
        PImage node = nodes.get(loc);
        if (node == null) {
            node = new PImage();
            node.addAttribute("url", toImageUrl(loc));
            node.setOffset(loc.x * IMG_SIZE, loc.y * IMG_SIZE);
            layer.addChild(node);
            nodes.put(loc, node);
        }
        return node;
    }

    private static URL toImageUrl(Point point) {
        try {
            return new URL(IMAGE_BASE_URL + "(" + (int) point.getX() + "," + (int) point.getY() + ")");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Point translated(Point start, int x, int y) {
        Point translated = ((Point) start.clone());
        translated.translate(x, y);
        return translated;
    }

}