package atdixon.piccolo.demo;

import edu.umd.cs.piccolo.PCanvas;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Demo extends JFrame {

    public static void main(String[] args) throws Exception {
        new Demo().go();
    }

    private final PCanvas canvas;
    private final List<Step> steps = new ArrayList<Step>();

    private Demo() throws Exception {
        setSize(800, 600);
        canvas = new PCanvas();
        setContentPane(canvas);
        registerSteps();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void go() {
        for (final Step step : steps) {
            try {
                step.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("done");
    }

    private void registerSteps() {
        steps.add(new Intro(canvas));
    }

}
