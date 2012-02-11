package atdixon.piccolo.example.selection;

import com.google.common.base.Function;
import edu.umd.cs.piccolo.PNode;

final class Fns {

    private Fns() {}

    static <T> Function<T, Void> select() {
        return (Function<T, Void>) SELECT;
    }

    static <T> Function<T, Void> deselect() {
        return (Function<T, Void>) DESELECT;
    }

    static <T> Function<T, Void> highlight() {
        return (Function<T, Void>) HIGHLIGHT;
    }

    static <T> Function<T, Void> unhighlight() {
        return (Function<T, Void>) UNHIGHLIGHT;
    }

    private static final Function HIGHLIGHT = new Function<PNode, Void>() {
        @Override
        public Void apply(PNode n) {
            if (n instanceof IHighlightable) {
                ((IHighlightable) n).highlight();
            }
            return null;
        }
    };

    private static final Function UNHIGHLIGHT = new Function<PNode, Void>() {
        @Override
        public Void apply(PNode n) {
            if (n instanceof IHighlightable) {
                ((IHighlightable) n).unhighlight();
            }
            return null;
        }
    };

    private static final Function<Object, Void> SELECT = new Function<Object, Void>() {
        @Override
        public Void apply(Object n) {
            if (n instanceof ISelectable) {
                ((ISelectable) n).select();
            }
            return null;
        }
    };

    private static final Function<Object, Void> DESELECT = new Function<Object, Void>() {
        @Override
        public Void apply(Object n) {
            if (n instanceof ISelectable) {
                ((ISelectable) n).deselect();
            }
            return null;
        }
    };

}
