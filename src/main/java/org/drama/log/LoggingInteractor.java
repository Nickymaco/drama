package org.drama.log;

public interface LoggingInteractor {
	static final String WHITESPACE = " ";
    /**
     * 捕获异常
     * @param e
     */
    void capture(Throwable e, Object... args);

    /**
     * 获取log解析器
     *
     */
    Logging getLogging();
}
