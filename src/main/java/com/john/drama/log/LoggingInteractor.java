package com.john.drama.log;

public interface LoggingInteractor {
	static final String WHITESPACE = " ";
    /**
     * 捕获异常
     * @param e
     */
    void catchException(Exception e);

    /**
     * 获取log解析器
     * @return
     */
    Logging getLogging();
}
