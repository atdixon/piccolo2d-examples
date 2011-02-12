package atdixon.piccolo.example;

import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.PFrame;

public class PTextExample extends PFrame {

    private static final String QUOTE =
        "Everyone generalizes from one example. At least, I do. -- Vlad Taltos (Issola, Steven Brust)";

    public static void main(String[] args) {
        new PTextExample();
    }

    @Override
    public void initialize() {

        final PText text = new PText(QUOTE);
        text.setConstrainWidthToTextWidth(false);
        text.setWidth(150);

        getCanvas().getLayer().addChild(text);
    }

}
