package org.drama.log.template;

import javax.annotation.PostConstruct;

import org.drama.log.Logging;
import org.drama.log.LoggingInteractor;

public class BasicLoggingTemplate implements LoggingInteractor {
	protected static final String STAGE_DEAL = "{} deal event<{}>";
	protected static final String STAGE_RECEVIE = "{} recevie event<{}>";
	protected static final String LAYER_BROADCAST = "{} Layer<{}> broadcast event<{}>";
	protected static final String LAYER_HANDING="{} Layer<{}> handing element<{}>";
	protected static final String STAGE_IS_RUNNING = "{} Stage<{}> is running!";
    protected static final String REGISTERED_LAYER = "{} registered Layer<{}>";
    protected static final String REGISTERED_ELEMENT = "{} registered element<{}>";
    protected static final String REGISTERED_EVENT = "{} registered event<{}>";
	
	private Logging logging;
	
	protected BasicLoggingTemplate(Logging logging) {
		this.logging = logging;
	}
	
	@Override
	public void capture(Throwable e, Object... args) {
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
