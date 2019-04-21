package org.drama.log.template;

import javax.annotation.PostConstruct;

import org.drama.log.Logging;
import org.drama.log.LoggingInteractor;

public class BasicLoggingTemplate implements LoggingInteractor {
	public static final String StageDeal = "{} deal event<{}>";
	public static final String StageRecevie = "{} recevie event<{}>";
	public static final String LayerBroadcast = "{} Layer<{}> broadcast event<{}>";
	public static final String LayerHanding="{} Layer<{}> handing element<{}>";
	
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
