package atdixon.piccolo.support;

import java.awt.font.LineBreakMeasurer;
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

}
