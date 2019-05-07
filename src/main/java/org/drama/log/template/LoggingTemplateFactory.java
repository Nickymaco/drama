package org.drama.log.template;

import java.util.Objects;

import org.drama.log.LoggingFactory;

public class LoggingTemplateFactory {
    private static DramaLoggingTemplate loggingTemplate;

    public static LayerLoggingTemplate getLayerLoggingTemplate(LoggingFactory loggingFactory) {
        if (Objects.isNull(loggingTemplate)) {
            loggingTemplate = new DramaLoggingTemplate(loggingFactory.logging());
        }
        return loggingTemplate;
    }

    public static StageLoggingTemplate getStageLoggingTemplate(LoggingFactory loggingFactory) {
        if (Objects.isNull(loggingTemplate)) {
            loggingTemplate = new DramaLoggingTemplate(loggingFactory.logging());
        }
        return loggingTemplate;
    }
}
