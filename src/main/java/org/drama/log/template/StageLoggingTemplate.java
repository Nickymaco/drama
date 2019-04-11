package org.drama.log.template;

import org.drama.common.MessageTemplate;
import org.drama.event.Event;
import org.drama.log.Logging;

class StageLoggingTemplate extends BasicLoggingTemplate implements IStageLoggingTemplate {
    public StageLoggingTemplate(Logging logging) {
        super(logging);
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
		
		this.getLogging().info(MessageTemplate.inst().getLogStageRecevie(), PREFIX, build);
	}

	@Override
	public void logDealEvent(Event event) {
		this.getLogging().info(MessageTemplate.inst().getLogStageDeal(), PREFIX, event.getClass().getSimpleName());
	}
}
