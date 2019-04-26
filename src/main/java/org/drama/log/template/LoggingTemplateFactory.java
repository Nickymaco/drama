package org.drama.log.template;

import java.util.Objects;

import org.drama.log.LoggingFactory;
import org.drama.log.LoggingInteractor;

public class LoggingTemplateFactory {
	private static StageLoggingTemplate loggingTemplate;
	
	public static ILayerLoggingTemplate getLayerLoggingTemplate(LoggingFactory loggingFactory) {
		if(Objects.isNull(loggingTemplate)) {
			loggingTemplate = new StageLoggingTemplate(loggingFactory.logging());
		}
		return loggingTemplate;
	}
	
	public static IStageLoggingTemplate getStageLoggingTemplate(LoggingFactory loggingFactory) {
		if(Objects.isNull(loggingTemplate)) {
			loggingTemplate = new StageLoggingTemplate(loggingFactory.logging());
		}
		return loggingTemplate;
	}
}
