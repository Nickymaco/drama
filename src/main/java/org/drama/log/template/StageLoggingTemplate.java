package org.drama.log.template;

import org.drama.event.Event;
import org.drama.log.Logging;

class StageLoggingTemplate implements IStageLoggingTemplate {
    private Logging logging;

    public StageLoggingTemplate(Logging logging) {
        this.logging = logging;
    }

    @Override
    public void catchException(Exception e) {
    	logging.error(e, "Stage occurred exception");
    }

    @Override
    public Logging getLogging() {
        return this.logging;
    }

	@Override
	public void logRecevieEvent(Event[] events) {
		if(events == null || events.length == 0) {
			return;
		}
		
		StringBuilder build = new StringBuilder();
		
		for(int i=0; i< events.length; i++) {
			if(i != 0) {
				build.append(WHITESPACE);
			}
			build.append(events[i].getClass().getSimpleName());
		}
		
		this.logging.info("{} recevie event<{}>", PREFIX, build);
	}

	@Override
	public void logDealEvent(Event event) {
		this.logging.info("{} deal event<{}>", PREFIX, event.getClass().getSimpleName());
	}
}
