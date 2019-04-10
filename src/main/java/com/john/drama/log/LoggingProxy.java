package com.john.drama.log;

/**
 * Logging类静态代理
 */
class LoggingProxy implements Logging {
	private Logging logging;
	
	public LoggingProxy(Logging logging) {
		this.logging = logging;
	}

	@Override
	public void debug(String message, Object... args) {
		if(!this.enableLevel(LoggingLevel.debug)) {
			return;
		}
		this.logging.debug(message, args);
	}

	@Override
	public void info(String message, Object... args) {
		if(!this.enableLevel(LoggingLevel.info)) {
			return;
		}
		this.logging.info(message, args);
	}

	@Override
	public void warn(String message, Object... args) {
		if(!this.enableLevel(LoggingLevel.warn)) {
			return;
		}
		this.logging.warn(message, args);
	}

	@Override
	public void error(Throwable e, String message, Object... args) {
		if(!this.enableLevel(LoggingLevel.error)) {
			return;
		}
		this.logging.error(e, message, args);
	}

	@Override
	public boolean enableLevel(LoggingLevel level) {
		if(this.logging == null) {
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
		if(obj == null || this.logging == null) {
			return false;
		}

		if(!(obj instanceof LoggingProxy)) {
			return false;
		}

		LoggingProxy compareObj = (LoggingProxy)obj;

		if(compareObj.logging == null) {
			return false;
		}

		return this.logging.equals(compareObj.logging);
	}
}
