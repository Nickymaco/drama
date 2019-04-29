package org.drama.core;

import org.drama.log.LoggingFactory;
import org.drama.security.Signature;

import java.io.Serializable;

public interface Configuration extends Serializable {
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
     * 签名, 每一个 Stage 应该需要一个签名注册到 kernel 里
     */
    Signature getSignature();
}