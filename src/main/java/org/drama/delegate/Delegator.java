package org.drama.delegate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drama.vo.BiParameterValueObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 委托传递器
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

    public static <T> void forEach(T[] arr, BiFunction<T, Integer, Boolean> handler) {
        if(ArrayUtils.isEmpty(arr) || Objects.isNull(handler)) {
            return;
        }

        for(int i=0,j=arr.length; i<j; i++) {
            if(handler.apply(arr[i], i)) {
                return;
            }
        }
    }

    public static <T> void forEach(T[] arr, Consumer<BiParameterValueObject<T, Integer>> handler) {
        if(ArrayUtils.isEmpty(arr) || Objects.isNull(handler)) {
            return;
        }

        BiParameterValueObject<T, Integer> consumer = new BiParameterValueObject<>();

        for(int i=0,j=arr.length; i<j; i++) {
            consumer.setParam1(arr[i]);
            consumer.setParam2(i);
            action(handler, consumer);
        }
    }

    public static <T> void forEach(Collection<T> coll, BiFunction<T, Integer, Boolean> handler) {
        if(CollectionUtils.isEmpty(coll) || Objects.isNull(handler)) {
            return;
        }

        Iterator<T> iterator = coll.iterator();

        int i=0;
        while(iterator.hasNext()) {
            if(handler.apply(iterator.next(), i)) {
                return;
            }
            i++;
        }
    }
}