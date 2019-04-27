package org.drama.core;

import java.io.Serializable;

public interface Render extends Serializable {
    String ERROR_MSG = "Stage service occurred exception";
    String UNFOUND_EVENT_MSG = "Any event unfound";
    String ABEND_MSG = "The proccess occurred abend";

    int SUCCESS = 0;
    int FAILURE = 1;

    /**
     * 响应码
     */
    int getCode();

    /**
     * 响应输出
     */
    Object getModel();

    /**
     * 响应消息
     */
    String getMessage();
}
