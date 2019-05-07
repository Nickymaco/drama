package org.drama.delegate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drama.vo.BiParameterValueObject;

/**
 * 委托传递器
 */
public final class Delegator {
	/**
	 * Delegate no parameter function
	 * @param delegate no parameter function
	 */
    public static void action(Runnable delegate) {
        if (delegate == null) {
            return;
        }

        delegate.run();
    }

    /**
     * Deleget one parameter function
     * @param <P> Parameter Type
     * @param delegate one parameter function
     * @param arg function parameter
     */
    public static <P> void action(Consumer<P> delegate, P arg) {
        if (delegate == null) {
            return;
        }

        delegate.accept(arg);
    }

    /**
     * Delegate one parameter with return function
     * @param <T> parameter type
     * @param <R> return type
     * @param delegate one paraemter function
     * @param arg function parameter
     * @return {@code R} type return
     * @throws NullPointerException
     */
    public static <T, R> R func(Function<T, R> delegate, T arg) throws NullPointerException {
        if (delegate == null) {
            return null;
        }
        return delegate.apply(arg);
    }

    /**
     * Array foreach deletegate
     * @param <T> Array item type
     * @param arr foreach target
     * @param handler {@code Integer} is the item position, Boolean {@code true} break foreach or {@code false} continue
     */
    public static <T> void forEach(T[] arr, BiFunction<T, Integer, Boolean> handler) {
        if (ArrayUtils.isEmpty(arr) || Objects.isNull(handler)) {
            return;
        }

        for (int i = 0, j = arr.length; i < j; i++) {
            if (handler.apply(arr[i], i)) {
                return;
            }
        }
    }

    /**
     * Array foreach deletegate
     * @param <T> Array item type
     * @param arr foreach target
     * @param handler {@code Integer} is the item position
     */
    public static <T> void forEach(T[] arr, Consumer<BiParameterValueObject<T, Integer>> handler) {
        if (ArrayUtils.isEmpty(arr) || Objects.isNull(handler)) {
            return;
        }

        BiParameterValueObject<T, Integer> consumer = new BiParameterValueObject<>();

        for (int i = 0, j = arr.length; i < j; i++) {
            consumer.setParam1(arr[i]);
            consumer.setParam2(i);
            action(handler, consumer);
        }
    }

    /**
     * Collection foreach deletegate
     * @param <T> Collection item type
     * @param coll foreach target
     * @param handler {@code Integer} is the item position, Boolean {@code true} break foreach or {@code false} continue
     */
    public static <T> void forEach(Collection<T> coll, BiFunction<T, Integer, Boolean> handler) {
        if (CollectionUtils.isEmpty(coll) || Objects.isNull(handler)) {
            return;
        }

        Iterator<T> iterator = coll.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            if (handler.apply(iterator.next(), i)) {
                return;
            }
            i++;
        }
    }
}