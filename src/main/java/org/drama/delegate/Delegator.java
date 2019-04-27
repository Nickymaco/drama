package org.drama.delegate;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 委托传递器
 *
 * @author john
 */
public final class Delegator {
    public static void action(Runnable delegate) {
        if (delegate == null) {
            return;
        }

        delegate.run();
    }

    public static <P> void action(Consumer<P> delegate, P arg) {
        if (delegate == null) {
            return;
        }

        delegate.accept(arg);
    }

    public static <T, R> R func(Function<T, R> delegate, T arg) throws NullPointerException {
        if (delegate == null) {
            return null;
        }
        return delegate.apply(arg);
    }
}