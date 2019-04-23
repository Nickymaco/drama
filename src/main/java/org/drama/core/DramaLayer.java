package org.drama.core;

import java.util.Objects;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.OccurredException;
import org.drama.log.LoggingFactory;
import org.drama.log.template.ILayerLoggingTemplate;
import org.drama.log.template.LoggingTemplateFactory;

/**
 * 默认逻辑处理层，如果没有指定逻辑处理层，舞台默认使用它进行构建
 */
public class DramaLayer implements Layer {
    private ILayerLoggingTemplate logging;
    private LoggingFactory loggingFactory;
    private Kernel kernel;
    private Configuration configuration;

    @Override
	public ImmutableSet<Element> getElements() {
		return null;
	}

    @Override
    public void broadcast(Event event, BroadcastLisenter broadcasetListener) {
		if(Objects.isNull(event)) {
			throw OccurredException.illegalBroadcastEvent(this, event);
		}
		
		// 设置当前逻辑处理层
		event.getContext().setCurrentLayer(this);
		
		final DramaLayer that = this;
		
		kernel.notifyHandler(this, event, (l) -> {
			// 打印日志
			that.getLogging().broadcast(l.getName(), event);
		},(e) -> {
			if(Objects.nonNull(broadcasetListener)) {
				broadcasetListener.setHandingStatus(e.getHandingStatus());
			}
		});
    }

	protected ILayerLoggingTemplate getLogging() {
		if(Objects.isNull(logging)) {
			logging = LoggingTemplateFactory.getLayerLoggingTemplate(LoggingFactory.Null);
		}
    	return logging;
    }

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		if(Objects.equals(configuration, this.configuration)) {
			return;
		}
		
		this.configuration = configuration;
		
		if(Objects.nonNull(configuration)) {
			if(!Objects.equals(kernel, configuration.getKernel())) {
				kernel = Objects.requireNonNull(configuration.getKernel());
			}
			
			if(!Objects.equals(loggingFactory, configuration.getLoggingFactory())) {
				loggingFactory = configuration.getLoggingFactory();
			}
		}
	}
}
