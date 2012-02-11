package atdixon.java2d.example;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

/**
 * This class demonstrates how to line-break and draw a paragraph
 * of text using LineBreakMeasurer and TextLayout.
 *
 * This class constructs a LineBreakMeasurer from an
 * AttributedCharacterIterator.  It uses the LineBreakMeasurer
 * to create and draw TextLayouts (lines of text) which fit within
 * the Component's width.
 */
public class LineBreakSample extends Component {

    private static final FontRenderContext DEFAULT_FRC =
                                new FontRenderContext(null, false, false);

    private static final Hashtable map = new Hashtable();
    static {
        map.put(TextAttribute.SIZE, new Float(18.0));
        map.put(TextAttribute.FAMILY, Font.MONOSPACED);
    }

    private static final String VAN_GOGH_BIO =
        "Many people\n believe that Vincent van Gogh painted his best works " +
        "during the two-year period he spent in Provence. Here is where he " +
        "painted The Starry Night--which some consider to be his greatest " +
        "work of all. However, as his artistic brilliance reached new heights " +
        "in Provence, his physical and mental health plummeted. ";

    private static final String XSTRING =
        "SpaghettiPaletiManillaVanillaCowpokeAlley";

    private static AttributedString CONTENT = new AttributedString(XSTRING, map);

    // The LineBreakMeasurer used to line-break the paragraph.
    private LineBreakMeasurer lineMeasurer;

    // The index in the LineBreakMeasurer of the first character
    // in the paragraph.
    private int paragraphStart;

    // The index in the LineBreakMeasurer of the first character
    // after the end of the paragraph.
    private int paragraphEnd;

    public LineBreakSample(AttributedCharacterIterator paragraph) {

        FontRenderContext frc = DEFAULT_FRC;

        paragraphStart = paragraph.getBeginIndex();
        paragraphEnd = paragraph.getEndIndex();

        // Create a new LineBreakMeasurer from the paragraph.
        lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        System.err.println(VAN_GOGH_BIO.length());
        System.err.println(paragraphStart + "," + paragraphEnd + ":" + lineMeasurer.getPosition());
    }

    public void paint(Graphics g) {

        Graphics2D graphics2D = (Graphics2D) g;

        // Set formatting width to width of Component.
        Dimension size = getSize();
        float formatWidth = (float) size.width;

        float drawPosY = 0;

        lineMeasurer.setPosition(paragraphStart);

        // Get lines from lineMeasurer until the entire
        // paragraph has been displayed.
        while (lineMeasurer.getPosition() < paragraphEnd) {

            // Retrieve next layout.
            
            int no = lineMeasurer.nextOffset(formatWidth);
            int limit = no;
            if (limit < XSTRING.length()) {
                for (int i = no - 1; i > lineMeasurer.getPosition(); --i) {
                    char c = XSTRING.charAt(i);
                    if (Character.isUpperCase(c)) {
                        limit = i;
                        break;
                    }
                }
            }

            TextLayout layout = lineMeasurer.nextLayout(formatWidth, limit, false);

            // Move y-coordinate by the ascent of the layout.
            drawPosY += layout.getAscent();

            // Compute pen x position.  If the paragraph is
            // right-to-left, we want to align the TextLayouts
            // to the right edge of the panel.
            float drawPosX;
            if (layout.isLeftToRight()) {
                drawPosX = 0;
            }
            else {
                drawPosX = formatWidth - layout.getAdvance();
            }

            // Draw the TextLayout at (drawPosX, drawPosY).
            layout.draw(graphics2D, drawPosX + (formatWidth - layout.getAdvance()) / 2, drawPosY);

            // Move y-coordinate in preparation for next layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }
    }

    public static void main(String[] args) {
        showInFrame(new LineBreakSample(CONTENT.getIterator()), "Line Break Sample");
    }

    private static void showInFrame(Component component, String title) {
        Frame frame = new Frame(title);
        frame.add(component);
	    frame.setBackground(Color.white);
        frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        frame.setSize(400, 250);
        frame.show();

        component.requestFocus();
    }
}
