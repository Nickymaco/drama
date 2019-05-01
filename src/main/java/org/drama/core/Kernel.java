package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.vo.BiParameterValueObject;

import java.util.function.Consumer;
import java.util.function.Function;

interface Kernel {
    /**
     * 注册元素
     *
     * @param element 需要被注册的元素
     */
    Layer registerElement(Element element);

    /**
     * 注册元素
     *
     * @param element      需要被注册的元素
     * @param onRegistered 完成元素注册回调通知有哪些事件被注册了
     */
    Layer registerElement(Element element, Consumer<Class<? extends Event>[]> onRegistered);

    /**
     * 通知事件句柄
     *
     * @param layer        要执行广播的逻辑处理层
     * @param event        要广播的事件
     * @param onPreHanding 在每一个逻辑处理层开始广播前执行
     * @param onCompleted  事件完成回调，如果 {@code true} 则退出当前逻辑层不再往下执行
     */
    void notifyHandler(Layer layer, Event event, Consumer<LayerContainer> onPreHanding, Consumer<ElementContainer> onCompleted);

    /**
     * 获取逻辑处理层
     */
    ImmutableSet<Layer> getlayers();

    /**
     * 获取逻辑处理层注册的元素
     *
     * @param layer 逻辑处理层
     * @return 已注册元素
     */
    ImmutableSet<Element> getElements(Layer layer);

    /**
     * Layer 构造工厂
     *
     * @param generator 构造工厂委托
     */
    void setLayerGenerator(Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> generator);

    /**
     * Layer 构造工厂
     */
    Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> getLayerGenerator();

    /**
     * 重置
     *
     * @return 返回 {@code true} 表示重置成功
     */
    boolean reset();
}