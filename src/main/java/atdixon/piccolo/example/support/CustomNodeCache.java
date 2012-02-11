package atdixon.piccolo.example.support;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PPickPath;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * A node cache (like {@link edu.umd.cs.piccolox.nodes.PNodeCache} that can be
 * enabled or disabled dynamically.
 */
public class CustomNodeCache extends PNode {

    private transient Image cache;
    private boolean validating;

    public boolean isNodeCacheEnabled(PPaintContext pc) {
        return false;
    }

    public void invalidatePaint() {
        if (!validating) {
            super.invalidatePaint();
        }
    }

    public void repaintFrom(final PBounds localBounds, final PNode childOrThis) {
        if (!validating) {
            super.repaintFrom(localBounds, childOrThis);
            internalInvalidateCache();
        }
    }

    public void fullPaint(final PPaintContext pc) {
        if (validating || !isNodeCacheEnabled(pc)) {
            super.fullPaint(pc);
        }
        else {
            final Graphics2D g2 = pc.getGraphics();
            g2.drawImage(internalGetCache(), (int) getX(), (int) getY(), null);
        }
    }

    protected boolean pickAfterChildren(final PPickPath pickPath) {
        return false;
    }

    private void internalInvalidateCache() {
        cache = null;
    }

    private Image internalGetCache() {
        if (cache == null) {
            validating = true;
            resetBounds();
            cache = internalCreateImageCache();
            final PBounds fb = getFullBoundsReference();
            setBounds(fb.getX(), fb.getY(), cache.getWidth(null), cache.getHeight(null));
            validating = false;
        }
        return cache;
    }

    private Image internalCreateImageCache() {
        return toImage();
    }

}
