package com.john.drama.core;

import java.util.Set;
import java.util.TreeSet;

import com.john.drama.event.Event;
import com.john.drama.exception.OccurredException;
import com.john.drama.log.Logging;
import com.john.drama.log.LoggingAdapter;
import com.john.drama.log.LoggingFactory;
import com.john.drama.log.template.IStageLoggingTemplate;
import com.john.drama.log.template.LoggingTemplateFactory;

/**
 * 默认舞台，执行逻辑处理层时按照线性关系依次执行
 */
class SequenceLayerStage extends AbstractStage {
    private IStageLoggingTemplate stageLogging;
    private Set<Layer> layerSet = new TreeSet<>();

    public SequenceLayerStage(LoggingFactory loggingFactory) {
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

   /**
    * 默认严格策略，只要有一个逻辑处理层返回不成功则不继续往下执行
    */
	@Override
	public void playDeal(Event event) throws OccurredException {
        if(event == null) {
            return;
        }

        if(this.layerSet == null || this.layerSet.size() == 0) {
            return;
        }
        
        for(Layer layer : this.layerSet) {
            BroadcastResult broadcastResult = null;
            
			try {
				broadcastResult = layer.broadcast(event);
			} catch (OccurredException e) {
				throw OccurredException.occurredPlayError(e, event);
			}

            if(broadcastResult.getStatus() == BroadcastTracer.Breakdown) {
            	StageRender render = getCurrentRender();
            	render.setCode(Render.WARNING);
            	render.setMessage("业务执行非正常结束");
                break;
            }
        }

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
