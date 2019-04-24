package org.drama.log.template;

import javax.annotation.PostConstruct;

import org.drama.log.Logging;
import org.drama.log.LoggingInteractor;

public class BasicLoggingTemplate implements LoggingInteractor {
	protected static final String STAGE_DEAL = IStageLoggingTemplate.PREFIX + " deal event<{}>";
	protected static final String STAGE_RECEVIE = IStageLoggingTemplate.PREFIX + " recevie event<{}>";
	protected static final String LAYER_BROADCAST = ILayerLoggingTemplate.PREFIX + " Layer<{}> broadcast event<{}>";
	protected static final String LAYER_HANDING = ILayerLoggingTemplate.PREFIX + " Layer<{}> handing element<{}>";
	protected static final String STAGE_IS_RUNNING = IStageLoggingTemplate.PREFIX + " Stage<{}> is running!";
    protected static final String REGISTERED_LAYER = IStageLoggingTemplate.PREFIX + " registered Layer<{}>";
    protected static final String REGISTERED_ELEMENT = IStageLoggingTemplate.PREFIX + " registered element<{}>";
    protected static final String REGISTERED_EVENT = IStageLoggingTemplate.PREFIX + " registered event<{}>";
	
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
