package org.drama.common;

import java.io.IOException;
import java.util.Properties;

public final class MessageTemplate {
	private static MessageTemplate msgTemp;
	private String renderError;
	private String renderUnfoundEvent;

	private String logStageDeal;
	private String logStageRecevie;
	private String logLayerBroadcast;
	private String logLayerHanding;

	private String exPlayError;
	private String exCastLayer;
	private String exIllegalEvent;
	private String exIllegalBoradcast;
	private String exElemHandingError;
	
	private MessageTemplate() {
		Properties config = new Properties();
		try {
			config.load(this.getClass().getClassLoader().getResourceAsStream("messages.properties"));
			this.renderError = config.getProperty("render.error");
			this.renderUnfoundEvent = config.getProperty("render.unfound-event");
			this.logStageDeal = config.getProperty("log.stage.deal");
			this.logStageRecevie = config.getProperty("log.stage.recevie");
			this.logLayerBroadcast = config.getProperty("log.layer.broadcast");
			this.logLayerHanding = config.getProperty("log.layer.handing");
			this.exPlayError = config.getProperty("ex.play-error");
			this.exCastLayer = config.getProperty("ex.cast-layer");
			this.exIllegalEvent = config.getProperty("ex.illegal-event");
			this.exIllegalBoradcast = config.getProperty("ex.illegal-broadcast");
			this.exElemHandingError = config.getProperty("ex.elem-handing-error");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static MessageTemplate inst() {
		if(msgTemp == null) {
			msgTemp = new MessageTemplate();
		}
		return msgTemp;
	}

	public String getRenderError() {
		return renderError;
	}

	public String getRenderUnfoundEvent() {
		return renderUnfoundEvent;
	}

	public String getLogStageDeal() {
		return logStageDeal;
	}

	public String getLogStageRecevie() {
		return logStageRecevie;
	}

	public String getLogLayerBroadcast() {
		return logLayerBroadcast;
	}

	public String getLogLayerHanding() {
		return logLayerHanding;
	}

	public String getExPlayError() {
		return exPlayError;
	}

	public String getExCastLayer() {
		return exCastLayer;
	}

	public String getExIllegalEvent() {
		return exIllegalEvent;
	}

	public String getExIllegalBoradcast() {
		return exIllegalBoradcast;
	}

	public String getExElemHandingError() {
		return exElemHandingError;
	}
}
