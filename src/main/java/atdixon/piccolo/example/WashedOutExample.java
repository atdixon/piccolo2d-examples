package atdixon.piccolo.example;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.PFrame;

import java.awt.*;

public class WashedOutExample extends PFrame {

    public static void main(String[] args) {
        new WashedOutExample();
    }

    public void initialize() {
        PPath line1 = PPath.createLine(760000, 7890000, 761100, 7891110);
        line1.setStroke(new BasicStroke(1.0f));
        //line1.setStroke(new PFixedWidthStroke(1.0f));
        //line1.setStroke(new PFixedWidthStrokeMRO(1.0f));
        line1.setStrokePaint(Color.BLUE);

        getCanvas().setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        getCanvas().setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        getCanvas().setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);

        getCanvas().getLayer().addChild(line1);
        getCanvas().getCamera().animateViewToCenterBounds(
                    getCanvas().getLayer().getFullBounds(), true, 100);
    }

}
