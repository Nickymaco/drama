package org.drama.log.template;

import static org.joor.Reflect.on;

import org.drama.log.Logging;
import org.drama.log.LoggingInteractor;

public class LoggingTemplateFactory {
	private static LoggingInteractor basicLoggingInteractor;
	private static ILayerLoggingTemplate layerLoggingTemplate;
	private static IStageLoggingTemplate stageLoggingTemplate;
	
	public static LoggingInteractor getBasicLoggingTemplate(Logging logging) {
		if(basicLoggingInteractor == null) {
			basicLoggingInteractor = new BasicLoggingTemplate(logging);
		}
		return basicLoggingInteractor;
	}
	
	public static ILayerLoggingTemplate getLayerLoggingTemplate(Logging logging) {
		if (layerLoggingTemplate == null) {
			layerLoggingTemplate = getTemplate(LayerLoggingTemplate.class, logging);
		}
		return layerLoggingTemplate;
	}
	
	public static IStageLoggingTemplate getStageLoggingTemplate(Logging logging) {
		if(stageLoggingTemplate == null) {
			stageLoggingTemplate = getTemplate(StageLoggingTemplate.class, logging);
		}
		return stageLoggingTemplate;
	}
	
	public static <T extends LoggingInteractor> T getTemplate(Class<? extends T> clazz, Object... args) {
		return on(clazz).create(args).get();
	}
}
