package atdixon.piccolo.example;

import edu.umd.cs.piccolo.*;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PObjectOutputStream;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;
import java.io.*;

/**
 * Demonstrate/test Piccolo's node serialization behavior.
 *
 * How this class works:
 *    Creates a simple scenegraph. If you double-click on the canvas, it serializes the scene
 *    to a temp file (and logs the name of this file to stdout).
 *
 *    You can restart the example providing a filename as a single command-line argument and
 *    this class will load the previously saved scene from the file.
 */
public class SerializeExample extends PFrame {

    public static void main(String[] args) {
        new SerializeExample(args.length > 0 ? args[0] : "nothing");
    }

    public SerializeExample(String filename) {
        super(SerializeExample.class.getSimpleName(), false, loadOrCreateCanvas(new File(filename)));
    }

    private static PCanvas loadOrCreateCanvas(File file) {
        return file.exists() ? load(file) : new PCanvas();
    }

    private static PCanvas load(File file) {
        ObjectInputStream in = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            in = new ObjectInputStream(fin);
            // @see PUtil.createBasicScenegraph()
            final PRoot root = new PRoot();
            final PLayer layer = (PLayer) in.readObject();
            final PCamera camera = new PCamera();
            root.addChild(camera);
            root.addChild(layer);
            camera.addLayer(layer);
            System.out.println("Loaded canvas from: " + file);
            return new PCanvas() {
                @Override
                protected PCamera createDefaultCamera() {
                    return camera;
                }
            };
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) { try { in.close(); } catch(IOException e) { /* nop */ } }
        }
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();
        final PCamera camera = canvas.getCamera();

        PPath rect = PPath.createRectangle(0, 0, 50, 50);
        rect.setPaint(Color.RED);
        layer.addChild(rect);

        PNode node = new PNode();
        node.setBounds(50, 50, 100, 100);
        node.setPaint(Color.green);
        layer.addChild(node);

        canvas.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent event) {
                if (event.getClickCount() == 2) {
                    PObjectOutputStream oos = null;
                    try {
                        File out = File.createTempFile(SerializeExample.class.getSimpleName(), "");
                        oos = new PObjectOutputStream(new FileOutputStream(out));
                        oos.writeObjectTree(layer);
                        System.out.println("Wrote: " + out);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (oos != null) { try { oos.close(); } catch (IOException e) { /* no-op */ } }
                    }
                }
            }
        });
    }

}