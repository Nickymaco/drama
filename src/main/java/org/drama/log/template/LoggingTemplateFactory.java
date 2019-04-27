package org.drama.log.template;

import org.drama.log.LoggingFactory;

import java.util.Objects;

public class LoggingTemplateFactory {
    private static StageLoggingTemplate loggingTemplate;

    public static ILayerLoggingTemplate getLayerLoggingTemplate(LoggingFactory loggingFactory) {
        if (Objects.isNull(loggingTemplate)) {
            loggingTemplate = new StageLoggingTemplate(loggingFactory.logging());
        }
        return loggingTemplate;
    }

    public static IStageLoggingTemplate getStageLoggingTemplate(LoggingFactory loggingFactory) {
        if (Objects.isNull(loggingTemplate)) {
            loggingTemplate = new StageLoggingTemplate(loggingFactory.logging());
        }
        return loggingTemplate;
    }
}
