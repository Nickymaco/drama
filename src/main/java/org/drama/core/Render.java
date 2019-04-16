package org.drama.core;

import java.io.Serializable;

public interface Render extends Serializable {
	static final int SUCCESS = 0;
	static final int FAILURE = 1;
    /**
     * 响应码
     * @return
     */
    int getCode();

    /**
     * 响应输出
     * @return
     */
    Object getModel();

    /**
     * 响应消息
     * @return
     */
    String getMessage();
}
