package org.drama.core;

import java.io.Serializable;

public interface Render extends Serializable {
	public static final String ERROR_MSG = "Stage service occurred exception";
	public static final String UNFOUND_EVENT_MSG = "Any event unfound";
	public static final String ABEND_MSG="The proccess occurred abend";
	
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
