package org.drama.core;

import java.util.Set;
import java.util.TreeSet;

import org.drama.log.Logging;
import org.drama.log.LoggingAdapter;
import org.drama.log.LoggingFactory;
import org.drama.log.template.IStageLoggingTemplate;
import org.drama.log.template.LoggingTemplateFactory;

/**
 * 默认舞台，执行逻辑处理层时按照线性关系依次执行
 */
public class BasicStage extends AbstractStage {
    private IStageLoggingTemplate stageLogging;
    private Set<Layer> layerSet = new TreeSet<>();

    public BasicStage(LoggingFactory loggingFactory) {
        super();
        Logging logging = LoggingAdapter.delegateLogging(loggingFactory);
		this.stageLogging = LoggingTemplateFactory.getStageLoggingTemplate(logging);
    }
    
    @Override
	public void addLayer(Layer layer) {
    	if(layer instanceof AbstractLayer) {
    		((AbstractLayer)layer).setLogging(this.getStageLoggingTemplate().getLogging());
    	}
		super.addLayer(layer);
	}

	@Override
	protected IStageLoggingTemplate getStageLoggingTemplate() {
		return stageLogging;
	}

	@Override
	public Set<Layer> getLayers() {
		return this.layerSet;
	}
}
