package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPickPath;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.nodes.PComposite;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Various examples of node picking when we have a parent node with multiple children.
 */
public class PickingExample extends PFrame {

    public static void main(String[] args) {
        new PickingExample();
    }

    @Override
    public void initialize() {
        PCanvas canvas = getCanvas();
        PLayer layer = canvas.getLayer();

        OutliningGroup g1 = new OutliningGroup();
        g1.setStrokePaint(Color.red);
        layer.addChild(g1);
        g1.translate(100, 100);

        CompositeGroup g2 = new CompositeGroup();
        layer.addChild(g2);
        g2.translate(300, 300);

        PrePickingGroup g3 = new PrePickingGroup();
        g3.setStrokePaint(Color.blue);
        layer.addChild(g3);
        g3.translate(300, 100);

        PostPickingGroup g4 = new PostPickingGroup();
        g4.setStrokePaint(Color.cyan);
        layer.addChild(g4);
        g4.translate(100, 300);

        // remove pan handler and add drag handler so we can demo the pickable behavior of the nodes
        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.addInputEventListener(new PDragEventHandler());
    }

    /**
     * Demonstrates the use of {@link PComposite} to create a group of children nodes but
     * whose parent becomes the picked node when a child would otherwise have been picked.
     * One use case for this approach would be when you want child nodes to be able to be
     * clicked on but drag the parent as a whole.
     */
    static class CompositeGroup extends PComposite {

        CompositeGroup() {
            addChildren(createChildren());
        }

    }

    /**
     * This group uses default Piccolo2D picking behavior. Its children are pickable; we add some
     * behavior to listen for the group's full bounds change so that we can maintain a nice border
     * around the group.
     */
    static class OutliningGroup extends PPath {

        OutliningGroup() {
            setStrokePaint(Color.green);
            addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    redrawOutline();
                }
            });
            addChildren(createChildren());
        }

        protected void redrawOutline() {
            PBounds fb = getUnionOfChildrenBounds(null);
            setPathToRectangle((float) fb.getX(), (float) fb.getY(), (float) fb.getWidth(), (float) fb.getHeight());
        }

    }

    /**
     * Overrides {@link OutliningGroup} with a simple override of {@link PNode#pick(PPickPath)} so that it is
     * picked before any of its children.
     * The effect is that this node is picked before its children when it answers true for
     * {@link #fullIntersects(java.awt.geom.Rectangle2D)}.
     */
    static class PrePickingGroup extends OutliningGroup {

        @Override
        protected boolean pick(PPickPath path) {
            return true;
        }

    }

    /**
     * Overrides {@link OutliningGroup}, sets children as unpickable and relies on
     * {@link PNode#pickAfterChildren(edu.umd.cs.piccolo.util.PPickPath)} to achieve the pick.
     */
    static class PostPickingGroup extends OutliningGroup {

        PostPickingGroup() {
            setChildrenPickable(false);
            // We could override pickAfterChildren OR intersects to achieve a desired behavior; but here
            // we will rely on PPath's implementation of these methods (which will consider this node
            // intersected as long as it has a non-null paint/fill.)
            setPaint(Color.white);
        }

    }

    private static List<PNode> createChildren() {
        // helpful common children creation behavior
        List<PNode> nodes = new ArrayList<PNode>();
        PPath rect = PPath.createRectangle(0, 0, 20, 20);
        nodes.add(rect);
        PPath circ = PPath.createEllipse(0, 0, 20, 20);
        circ.setOffset(20, 20);
        nodes.add(circ);
        return nodes;
    }

}