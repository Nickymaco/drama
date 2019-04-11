package org.drama.log.template;

import javax.annotation.PostConstruct;

import org.drama.log.Logging;
import org.drama.log.LoggingInteractor;
import org.drama.log.LoggingLevel;

public class BasicLoggingTemplate implements LoggingInteractor {
	private Logging logging;
	
	protected BasicLoggingTemplate(Logging logging) {
		this.logging = logging;
	}
	
	@Override
	public void catchException(Exception e) {
		if(this.logging.enableLevel(LoggingLevel.debug)) {
			e.printStackTrace();
		}
		logging.error(e, e.getMessage());
	}

	@Override
	public Logging getLogging() {
		return this.logging;
	}
	
	@PostConstruct
	protected void init() {	
	}
}
