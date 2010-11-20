package atdixon.piccolo.console;

import edu.umd.cs.piccolox.PFrame;
import groovy.ui.Console;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;

public class Driver extends PFrame {
    
    private static final int MARGIN = 10;

    public static void main(String[] args) {
        new Driver();
    }

    @Override
    public void initialize() {
        setSize(800, 600);
        initConsole();
    }

    private void initConsole() {
        Console console = new Console();
        console.setVariable("frame", this);
        console.setShowScriptInOutput(false);
        console.setAutoClearOutput(true);
        console.run();
        ((JFrame) console.getFrame()).setLocation(getX() + getWidth() + MARGIN, getY());
        console.loadScriptFile(resourceAsFile("initial._groovy"));
    }

    private static File resourceAsFile(String res) {
        try {
            return new File(Driver.class.getResource(res).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
