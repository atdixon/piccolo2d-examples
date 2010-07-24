package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.pswing.PSwing;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class ScrollableTooltipExample extends PFrame {

    public static void main(String[] args) {
        new ScrollableTooltipExample();
    }

    ScrollableTooltipExample() {
        super("ScrollableTooltipExample", false, new PSwingCanvas());
    }
    
    @Override
    public void initialize() {
        setSize(800, 600);
        final PSwingCanvas canvas = (PSwingCanvas) getCanvas();
        PNode node = PPath.createRectangle(162, 100, 100, 62);
        node.setPaint(Color.lightGray);
        node.addAttribute("tooltip", "This is a few lines of scrollable content.".replace(' ', '\n'));
        canvas.getLayer().addChild(node);
        node.addInputEventListener(new TooltipHandler(canvas.getLayer()));
        getCanvas().removeInputEventListener(getCanvas().getPanEventHandler()); 
    }

    static class TooltipHandler extends PBasicInputEventHandler {
        private PSwing tooltip;
        private JTextArea textArea;
        private boolean stuck = false;
        TooltipHandler(PLayer layer) {
            textArea = new JTextArea(5, 20);
            textArea.setEditable(false);
            tooltip = new PSwing(new JScrollPane(textArea));
            tooltip.setPickable(false);
            tooltip.setVisible(false);
            layer.addChild(tooltip);
        }
        @Override
        public void mouseEntered(PInputEvent event) {
            PNode node = event.getPickedNode();
            String content = (String) node.getAttribute("tooltip");
            textArea.setText(content);
            tooltip.setVisible(true);
            tooltip.moveToFront();
        }
        @Override
        public void mouseMoved(PInputEvent event) {
            if (tooltip.getVisible() && !stuck) {
                PNode picked = event.getPickedNode();
                Point2D point = event.getPositionRelativeTo(picked);
                picked.localToParent(point);
                tooltip.setOffset(point);
            }
        }
        @Override
        public void mouseExited(PInputEvent event) {
            tooltip.setVisible(false || stuck);
            stuck = false;
        }
        @Override
        public void mouseClicked(PInputEvent event)  {
            stuck = true;
            tooltip.setPickable(true);
            tooltip.addInputEventListener(new PBasicInputEventHandler() {
                @Override
                public void mouseExited(PInputEvent event) {
                    tooltip.setPickable(false);
                    tooltip.setVisible(false);
                    stuck = false;
                }
            });
            event.setHandled(true);
        }
    }
}