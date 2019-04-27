package org.drama.log;

import java.util.Objects;

/**
 * Logging 类代理
 */
class LoggingProxy implements Logging {
    private Logging logging;

    private LoggingProxy(Logging logging) {
        this.logging = logging;
    }

    @Override
    public void debug(String message, Object... args) {
        if (!enableLevel(LoggingLevel.debug)) {
            return;
        }
        logging.debug(message, args);
    }

    @Override
    public void info(String message, Object... args) {
        if (!enableLevel(LoggingLevel.info)) {
            return;
        }
        logging.info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        if (!enableLevel(LoggingLevel.warn)) {
            return;
        }
        logging.warn(message, args);
    }

    @Override
    public void error(Throwable e, String message, Object... args) {
        if (!enableLevel(LoggingLevel.error)) {
            return;
        }

        if (enableLevel(LoggingLevel.debug)) {
            e.printStackTrace();
        }

        logging.error(e, message, args);
    }

    @Override
    public boolean enableLevel(LoggingLevel level) {
        if (Objects.isNull(logging)) {
            return false;
        }
        return this.logging.enableLevel(level);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj) || Objects.isNull(logging)) {
            return false;
        }

        if (!(obj instanceof LoggingProxy)) {
            return false;
        }

        LoggingProxy compareObj = (LoggingProxy) obj;

        if (Objects.isNull(compareObj.logging)) {
            return false;
        }

        return this.logging.equals(compareObj.logging);
    }

    public static Logging newInstance(LoggingFactory loggingFactory) {
        Logging logging = null;

        if (Objects.nonNull(loggingFactory)) {
            logging = loggingFactory.getLogging();
        }

        if (logging instanceof LoggingProxy) {
            return logging;
        }

        return new LoggingProxy(logging);
    }
}
