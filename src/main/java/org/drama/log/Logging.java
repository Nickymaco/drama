package org.drama.log;

public interface Logging {
    boolean enableLevel(LoggingLevel level);

    void debug(String message, Object... args);

    void info(String message, Object... args);

    void warn(String message, Object... args);

    void error(Throwable e, String message, Object... args);
}
