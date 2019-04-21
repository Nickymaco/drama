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
    private Kernel kernel;

    @Override
	public ImmutableSet<Element> getElements() {
		return null;
	}

	public final Kernel getKernel() {
		return kernel;
	}

	@Override
	public void setKernel(Kernel kernel) {
		this.kernel = kernel;
	}

    @Override
    public BroadcastResult broadcast(Event event) throws OccurredException {
		BroadcastResult result = new BroadcastResult(BroadcastTracer.Processing);
		
		if(event == null) {
		    throw OccurredException.illegalBroadcastEvent(this, event);
		}
		
		// 设置当前逻辑处理层
		event.getContext().setCurrentLayer(this);
		// 打印日志
		getLogging().logBroadcast(this, event);
		
		handingElement(event, result);
		
        return result;
    }

	protected void handingElement(Event event, final BroadcastResult result) throws OccurredException {
		getKernel().notifyHandler(this, event, (b) -> {
			if(b == Broken.Layer) {
				return true;
			}
			
			if(b == Broken.Stage) {
				result.setStatus(BroadcastTracer.Completed);
				return true;
			}
			
			return false;
		});
	}
	
	@Override
	public void setLoggingFactory(LoggingFactory loggingFactory) {
		if(Objects.isNull(loggingFactory)) {
			return;
		}
		
		logging = LoggingTemplateFactory.getLayerLoggingTemplate(loggingFactory);
	}

	protected ILayerLoggingTemplate getLogging() {
    	return logging;
    }

	
}
