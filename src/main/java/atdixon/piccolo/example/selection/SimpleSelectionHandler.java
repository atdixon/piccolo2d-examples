package atdixon.piccolo.example.selection;

import com.google.common.collect.Sets;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PNodeFilter;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

/**
 * Simplified but reduced-functionality version of PSelectionEventHandler.
 */
final class SimpleSelectionHandler extends PDragSequenceEventHandler {

    private static final int DASH_WIDTH = 5;
    private static final int NUM_STROKES = 10;

    private final Stroke[] strokes;
    private float strokeIdx = 0;

    private final PPath marquee = new PPath();

    private Point2D start;

    private Set<PNode> selected = new HashSet<PNode>();
    /** For debugging; how many nodes did we touch to determine selection. */
    private final Set<PNode> touched = new HashSet<PNode>();

    public SimpleSelectionHandler() {
        strokes = createStrokesCache();
        setEventFilter(new PInputEventFilter() {
            @Override
            public boolean acceptsEvent(PInputEvent e, int t) {
                return (!e.isMouseEvent() || e.getButton() == MouseEvent.BUTTON1)
                    && super.acceptsEvent(e, t);
            }
        });
    }

    public void clearMarqueeSelection() {
        if (getDragActivity() != null) {
            super.stopDragActivity(null);
        }
        marquee.removeFromParent();
        Utils.apply(Fns.<PNode>deselect(), selected);
        selected.clear();
    }

    private Stroke[] createStrokesCache() {
        final float[] dash = { DASH_WIDTH, DASH_WIDTH };
        final Stroke[] strokes = new Stroke[NUM_STROKES];
        for (int i = 0; i < NUM_STROKES; i++) {
            strokes[i] = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, dash, i);
        }
        return strokes;
    }

    @Override
    protected void startDrag(PInputEvent pe) {
        clearMarqueeSelection();
        super.startDrag(pe);
        start = pe.getCanvasPosition();
        marquee.setBounds(start.getX(), start.getY(), 0, 0);
        pe.getCamera().addChild(marquee);
    }

    @Override
    protected void drag(final PInputEvent pe) {
        super.drag(pe);
        final PBounds b = new PBounds();

        b.add(start);
        b.add(pe.getCanvasPosition());

        marquee.setPathToRectangle((float) b.x, (float) b.y, (float) b.width, (float) b.height);

        Utils.apply(Fns.<PNode>unhighlight(), touched);
        touched.clear();

        final PNodeFilter f = new PNodeFilter() {
            final PBounds lb = new PBounds();
            @Override
            public boolean accept(PNode n) {
                touched.add(n);
                lb.setRect(b);
                pe.getCamera().localToView(lb);
                n.globalToLocal(lb);
                return n.intersects(lb) && (n instanceof ISelectable);
            }
            @Override
            public boolean acceptChildrenOf(PNode n) {
                if (n instanceof PLayer)
                    return true;
                touched.add(n);
                lb.setRect(b);
                pe.getCamera().localToView(lb);
                n.globalToLocal(lb);
                return n.intersects(lb);
            }
        };

        // ...consider possible optimization knowing our latest select rectangle (just the delta)
        final Set<PNode> newlySelected = Sets.newHashSet();
        final PLayer layer = pe.getCamera().getLayer(0);
        layer.getAllNodes(f, newlySelected);

        Utils.apply(Fns.<PNode>highlight(), touched);

        Utils.applyToRelativeComplements(selected, newlySelected, Fns.<PNode>deselect(),
                Fns.<PNode>select());

        selected = newlySelected;
    }

    @Override
    protected void stopDragActivity(PInputEvent event) { }

    protected void startDragActivity(final PInputEvent pe) {
        super.startDragActivity(pe);
        // overwrite PDragSequenceEventHandler's delegate, we'll provide our
        // own b/c we want the PActivity state
        getDragActivity().setDelegate(new PActivity.PActivityDelegate() {
            @Override
            public void activityStarted(PActivity a) { }

            @Override
            public void activityStepped(PActivity a) {
                final int prev = (int) strokeIdx;
                strokeIdx = (strokeIdx + 0.5f) % NUM_STROKES;
                final int curr = (int) strokeIdx;
                if (strokeIdx != prev) {
                    marquee.setStroke(strokes[curr]);
                }
            }

            @Override
            public void activityFinished(PActivity a) { }
        });
    }

}
