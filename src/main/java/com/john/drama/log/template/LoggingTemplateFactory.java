package com.john.drama.log.template;

import static org.joor.Reflect.on;

import com.john.drama.log.Logging;
import com.john.drama.log.LoggingInteractor;

public class LoggingTemplateFactory {
	public static ILayerLoggingTemplate getLayerLoggingTemplate(Logging logging) {
		return getTemplate(LayerLoggingTemplate.class, logging);
	}
	
	public static IStageLoggingTemplate getStageLoggingTemplate(Logging logging) {
		return getTemplate(StageLoggingTemplate.class, logging);
	}
	
	public static <T extends LoggingInteractor> T getTemplate(Class<? extends T> clazz, Object... args) {
		return on(clazz).create(args).get();
	}
}
