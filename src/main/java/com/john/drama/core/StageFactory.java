package com.john.drama.core;

import java.util.List;

import com.john.drama.log.LoggingFactory;

public final class StageFactory {
	public static Stage getStage(LoggingFactory loggingFactory) {
		return StageFactory.getStage(null, loggingFactory);
	}
	
	public static Stage getStage(List<Layer> layers, LoggingFactory loggingFactory) {
		SequenceLayerStage stage = new SequenceLayerStage(loggingFactory);
		
		if(layers != null && layers.size() > 0) {
			for(Layer layer : layers) {
				stage.addLayer(layer);
			}
		}
		return stage;
	}
}
