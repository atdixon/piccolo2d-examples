package atdixon.piccolo.example.support;

import edu.umd.cs.piccolo.util.PPaintContext;

import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

public abstract class CustomBreakingText extends _PStyledText {

    /** Is the provided character a good candidate for breaking? */
    public abstract boolean isBreakableChar(char c);

    protected ArrayList extractLineBreaks(final AttributedCharacterIterator itr, final LineBreakMeasurer measurer) {
        ArrayList breakList;
        breakList = new ArrayList();
        while (measurer.getPosition() < itr.getEndIndex()) {
            if (constrainWidthToTextWidth) {
                measurer.nextLayout(Float.MAX_VALUE);
            }
            else {
                // if nextOffset is not end of the line make sure it is on a space or capitalize word boundary
                int no = measurer.nextOffset((float) Math.max(Math.ceil(getWidth() - insets.left - insets.right), 0));
                int limit = no;
                if (limit < itr.getEndIndex()) {
                    for (itr.setIndex(no); itr.getIndex() > measurer.getPosition(); itr.previous()) {
                        char c = itr.current();
                        if (isBreakableChar(c)) {
                            limit = itr.getIndex();
                            break;
                        }
                    }
                }

                measurer.nextLayout((float) Math.max(Math.ceil(getWidth() - insets.left - insets.right), 0), limit, false);
            }

            breakList.add(new Integer(measurer.getPosition()));
        }
        return breakList;
    }

    /**
     * Override {@paint} to achieve text layout centering.
     */
    protected void paint(final PPaintContext paintContext) {
        if (lines == null || lines.length == 0) {
            return;
        }

        final float x = (float) (getX() + insets.left);
        float y = (float) (getY() + insets.top);
        final float bottomY = (float) (getY() + getHeight() - insets.bottom);

        final Graphics2D g2 = paintContext.getGraphics();

        if (getPaint() != null) {
            g2.setPaint(getPaint());
            g2.fill(getBoundsReference());
        }

        float curX;
        LineInfo lineInfo;
        for (int i = 0; i < lines.length; i++) {
            lineInfo = lines[i];
            y += lineInfo.maxAscent;
            curX = x;

            if (bottomY < y) {
                return;
            }

            float totalAdvance = 0;
            for (Object o : lineInfo.segments) {
                SegmentInfo s = (SegmentInfo) o;
                totalAdvance += s.layout.getVisibleAdvance();
            }

            for (int j = 0; j < lineInfo.segments.size(); j++) {
                final SegmentInfo sInfo = (SegmentInfo) lineInfo.segments.get(j);
                final float width = sInfo.layout.getAdvance();

                if (sInfo.background != null) {
                    g2.setPaint(sInfo.background);
                    g2.fill(new Rectangle2D.Double(curX, y - lineInfo.maxAscent, width, lineInfo.maxAscent
                            + lineInfo.maxDescent + lineInfo.leading));
                }

                sInfo.applyFont(g2);

                // Manually set the paint - this is specified in the
                // AttributedString but seems to be
                // ignored by the TextLayout. To handle multiple colors we
                // should be breaking up the lines
                // but that functionality can be added later as needed
                g2.setPaint(sInfo.foreground);
                sInfo.layout.draw(g2, curX +  ((float) getWidth() -insets.left -insets.right - totalAdvance) / 2, y);

                // Draw the underline and the strikethrough after the text
                if (sInfo.underline != null) {
                    paintLine.setLine(x, y + lineInfo.maxDescent / 2, x + width, y + lineInfo.maxDescent / 2);
                    g2.draw(paintLine);
                }

                curX = curX + width;
            }
            System.err.println();

            y += lineInfo.maxDescent + lineInfo.leading;
        }
    }

}
