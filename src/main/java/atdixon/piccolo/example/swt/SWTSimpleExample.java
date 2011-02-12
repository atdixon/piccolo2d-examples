package atdixon.piccolo.example.swt;

import edu.umd.cs.piccolox.swt.PSWTCanvas;
import edu.umd.cs.piccolox.swt.PSWTPath;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.awt.*;

public final class SWTSimpleExample {

    public static Shell open(final Display display) {
        final Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());

        // create a new SWT canvas
        final PSWTCanvas canvas = new PSWTCanvas(shell, 0);

        PSWTPath rect = PSWTPath.createPolyline(
            new float[] {0, 100},
            new float[] {0, 100});
        rect.setStrokeColor(Color.BLUE);
        canvas.getLayer().addChild(rect);

        rect.scale(200);

        shell.open();
        return shell;
    }
    
    public static void main(final String[] args) {
        final Display display = new Display();
        final Shell shell = open(display);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
