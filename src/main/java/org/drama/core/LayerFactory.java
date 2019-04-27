package org.drama.core;

/**
 * 逻辑处理层工厂
 *
 * @author john
 */
public interface LayerFactory {
    /**
     * 根据class返回逻辑处理层
     *
     * @param clazz
     */
    Layer getLayer(Class<? extends Layer> clazz);

    /**
     * 根据名称和优先级返回逻辑处理层
     *
     * @param name
     * @param priority
     */
    Layer getLayer(LayerDescriptor descriptor);
}
