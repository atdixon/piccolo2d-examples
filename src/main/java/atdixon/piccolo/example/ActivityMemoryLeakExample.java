package atdixon.piccolo.example;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;

public class ActivityMemoryLeakExample extends PFrame {

    public static void main(String[] args) {
        new ActivityMemoryLeakExample();
    }

    @Override
    public void initialize() {
        final PLayer layer = getCanvas().getLayer();
        // Create the node that we expect get garbage collected.
        PNode node = PPath.createEllipse(20, 20, 20, 20);
        layer.addChild(node);
        // Create a WeakReference to the node so we can detect if it is gc'd.
        final WeakReference<PNode> ref = new WeakReference<PNode>(layer.getChild(0));
        // Create and execute an activity.
        ref.get().animateToPositionScaleRotation(0, 0, 5.0, 0, 1000);
        // Create a Timer that will start after the activity and repeat.
        new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Remove our reference to the node.
                layer.removeAllChildren();
                // Force garbage collection.
                System.gc();
                // This should print null if the node was successfully gc'd. (IT never does.)
                System.out.println(ref.get());
                // This prints 0 as expected.
                System.out.println(layer.getRoot().getActivityScheduler().getActivitiesReference().size());
            }
        }).start();
        // This will cause any previous activity references to clear.
        forceCleanupOfPriorActivities(layer);
    }

    private void forceCleanupOfPriorActivities(final PLayer layer) {
        new Thread() {
            @Override
            public void run() {
                // Wait 6 seconds before doing anything.
                try { Thread.sleep(6000); } catch (InterruptedException e) { }
                PActivity a = new PActivity(-1) {
                    @Override
                    protected void activityStep(long elapsedTime) {
                        System.out.println("cleanup activity");
                        terminate();
                    }
                };
                layer.getRoot().addActivity(a);
            }
        }.start();
    }

}
