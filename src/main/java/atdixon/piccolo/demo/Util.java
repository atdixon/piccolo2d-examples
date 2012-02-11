package atdixon.piccolo.demo;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.activities.PActivity;

import javax.swing.*;

class Util {

    static void startAfter(PActivity a, PActivity b, long plus) {
        a.setStartTime(b.getStartTime() + b.getDuration() + plus);
    }

    static void notifyAfter(final Object target, PActivity a) {
        PActivity notifier = new PActivity(0) {
            @Override
            protected void activityStarted() {
                synchronized (target) {
                    target.notify();
                }
            }
        };
        notifier.startAfter(a);
        a.getActivityScheduler().addActivity(notifier);
    }

    static void clear(final PLayer layer) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                layer.removeAllChildren();
            }
        });
    }

}
