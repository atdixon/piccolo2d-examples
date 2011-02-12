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
 * Various examples of groups of children nodes.
 */
public class CompositeGroupsExample extends PFrame {

    public static void main(String[] args) {
        new CompositeGroupsExample();
    }

    @Override
    public void initialize() {
        PCanvas canvas = getCanvas();
        PLayer layer = canvas.getLayer();

        OutliningGroup g1 = new OutliningGroup();
        layer.addChild(g1);
        g1.translate(100, 100);

        CompositeGroup g2 = new CompositeGroup();
        layer.addChild(g2);
        g2.translate(300, 300);

        PrePickingGroup g3 = new PrePickingGroup();
        layer.addChild(g3);
        g3.translate(300, 0);

        // remove pan handler and add drag handler so we can demo the pickable behavior of the nodes
        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.addInputEventListener(new PDragEventHandler());
    }

    /**
     * Demonstrates the use of {@link PComposite} to create a group of children nodes but
     * whose parent becomes the first-class picked node when a child is interacted with.
     * For example, a mouse click on any child will pick the parent.
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
     */
    static class PrePickingGroup extends OutliningGroup {

        @Override
        protected boolean pick(PPickPath path) {
            return true;
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