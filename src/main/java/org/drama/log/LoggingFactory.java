package org.drama.log;

public abstract class LoggingFactory {
    public static final class NullLogging extends LoggingFactory {
        @Override
        protected Logging getLogging() {
            return LoggingProxy.newInstance(null);
        }
    }

    public Logging logging() {
        return LoggingProxy.newInstance(this);
    }

    protected abstract Logging getLogging();
}
