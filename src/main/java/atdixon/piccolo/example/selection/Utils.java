package atdixon.piccolo.example.selection;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;

final class Utils {

    private Utils() {}

    /**
     * Set-theoretic difference between a and b. Invokes provided {@link Function}s with results of
     * a without b (a \ b) and b without a (b \ a).
     */
    static <T> void applyToRelativeComplements(Set<T> a, Set<T> b, Function<T, Void> aNotInB, Function<T, Void> bNotInA) {
        final Set<T> bCopy = Sets.newHashSet(b);
        final Iterator<T> ai = a.iterator();
        while (ai.hasNext()) {
            T inA = ai.next();
            if (!bCopy.remove(inA)) {
                aNotInB.apply(inA);
            }
        }
        for (T inBOnly : bCopy) {
            bNotInA.apply(inBOnly);
        }
    }

    static <T> void apply(Function<T, Void> f, Set<T> c) {
        for (T i : c) {
            f.apply(i);
        }
    }

}
