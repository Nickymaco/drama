package org.drama.log.template;

import org.drama.log.Logging;
import org.drama.log.LoggingInteractor;

public class BasicLoggingTemplate implements LoggingInteractor {
	protected static final String STAGE_DEAL = IStageLoggingTemplate.PREFIX + " addressed event<%s>";
	protected static final String STAGE_RECEVIE = IStageLoggingTemplate.PREFIX + " recevied event{ %s }";
	protected static final String LAYER_BROADCAST = ILayerLoggingTemplate.PREFIX + " Layer<%s> broadcast event<%s>";
	protected static final String LAYER_HANDING = ILayerLoggingTemplate.PREFIX + " Layer<%s> handing element<%s>";
	protected static final String STAGE_IS_RUNNING = IStageLoggingTemplate.PREFIX + " Stage<%s> is running!";
    protected static final String REGISTERED_LAYER = IStageLoggingTemplate.PREFIX + " registered Layer<%s>";
    protected static final String REGISTERED_ELEMENT = IStageLoggingTemplate.PREFIX + " registered element<%s>";
    protected static final String REGISTERED_EVENT = IStageLoggingTemplate.PREFIX + " registered event<%s>";
	
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
}
