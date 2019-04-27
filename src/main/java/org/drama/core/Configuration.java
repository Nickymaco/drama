package org.drama.core;

import org.drama.log.LoggingFactory;

import java.io.Serializable;

public interface Configuration extends Serializable {
    /**
     * 事件传播监听器
     */
    BroadcastLisenter getBroadcastLisenter();

    /**
     * 逻辑处理层工厂
     */
    LayerFactory getLayerFactory();

    /**
     * 注册元素
     */
    RegisterElementFactory getRegisterElementFactory();

    /**
     * 注册事件
     */
    RegisterEventFactory getRegisterEventFactory();

    /**
     * 添加日志
     */
    LoggingFactory getLoggingFactory();

    /**
     * 默认错误输出
     */
    Render defaultErrorRender();

    /**
     * 签名, 每一个 Stage 应该需要一个签名注册到 kernel 里
     */
    Signature getSignature();
}