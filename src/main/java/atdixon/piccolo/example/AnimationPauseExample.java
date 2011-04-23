package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import java.awt.Color;

/**
 * Demonstrates animating a ball across the screen with "pause" behavior invoked by mouse clicks.
 */
public class AnimationPauseExample extends PFrame {

    private static final int BALL_SIZE = 25;
    private static final int ELLAPSE_UNIT = 40;

    public static void main(String[] args) {
        new AnimationPauseExample();
    }

    @Override
    public void initialize() {
        final PCanvas canvas = getCanvas();
        final PLayer layer = canvas.getLayer();

        final PPath circle = PPath.createEllipse(0, 0, BALL_SIZE, BALL_SIZE);
        circle.setPaint(Color.red);

        layer.addChild(circle);

        final AnimateBallActivity a = new AnimateBallActivity(circle);
        layer.addActivity(a);

        canvas.removeInputEventListener(canvas.getPanEventHandler());
        canvas.removeInputEventListener(canvas.getZoomEventHandler());

        canvas.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent event) {
                a.togglePause();
            }
        });
    }

    private static class AnimateBallActivity extends PActivity {

        private long prevFrame = 0;
        private boolean paused = false;
        private final PPath circle;

        public AnimateBallActivity(PPath circle) {
            super(-1 /* run forever */);
            this.circle = circle;
        }

        @Override
        protected void activityStep(long elapsed) {
            super.activityStep(elapsed);
            if (paused) {
                return;
            }
            final long frame = elapsed / ELLAPSE_UNIT;
            if (frame != prevFrame) {
                circle.translate(1, 1);
                prevFrame = elapsed / ELLAPSE_UNIT;
            }
        }

        public void togglePause() {
            paused = !paused;
        }

    }
}