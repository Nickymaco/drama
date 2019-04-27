package org.drama.log;

public abstract class LoggingFactory {
    /**
     * 占位符
     */
    static final class Null extends LoggingFactory {
        @Override
        protected Logging getLogging() {
            return LoggingProxy.newInstance(null);
        }
    }

    public static final LoggingFactory NULL = new LoggingFactory.Null();

    public Logging logging() {
        return LoggingProxy.newInstance(this);
    }

    protected abstract Logging getLogging();
}
