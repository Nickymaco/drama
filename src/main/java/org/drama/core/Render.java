package org.drama.core;

import java.io.Serializable;
import java.util.Map;

public interface Render extends Serializable, AutoCloseable {
    int SUCCESS = 0;
    int FAILURE = 1;

    /**
     * 响应码
     */
    int getCode();

    /**
     * 设置响应码
     *
     * @param code 响应码
     */
    void setCode(int code);

    /**
     * 响应模型
     */
    Map<String, Object> getModel();

    /**
     * 设置响应模型
     *
     * @param model 响应模型
     */
    void setModel(Map<String, Object> model);

    /**
     * 响应消息
     */
    String getMessage();

    /**
     * 设置响应消息
     *
     * @param message 响应消息
     */
    void setMessage(String message);
}
