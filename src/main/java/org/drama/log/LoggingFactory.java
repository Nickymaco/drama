package org.drama.log;

public abstract class LoggingFactory {
	
	public Logging logging() {
		return LoggingProxy.newInstance(this);
	}
	
	protected abstract Logging getLogging();
}
