package org.drama.core;

import java.io.Serializable;
import java.util.UUID;

/**
 * 签名操作, 实现此接口时应该同时重写 equals 和 hashcode 方法
 */
public interface Signature {
    /**
     * 唯一标示
     */
    UUID getIdentity();

    /**
     * 签署对象
     */
    Serializable getSigner();
}
