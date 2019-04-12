package org.drama.common;

import java.io.IOException;
import java.util.Properties;

public final class MessageTemplate {
	private static MessageTemplate msgTemp;
	private String renderError;
	private String renderUnfoundEvent;
	private String renderAbend;
	private String logStageDeal;
	private String logStageRecevie;
	private String logLayerBroadcast;
	private String logLayerHanding;
	private String exPlayError;
	private String exCastLayer;
	private String exIllegalEvent;
	private String exIllegalBoradcast;
	private String exElemHandingError;
	
	private MessageTemplate() throws IOException {
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream("messages.properties"));
		this.renderError = prop.getProperty("render.error");
		this.renderUnfoundEvent = prop.getProperty("render.unfound-event");
		this.renderAbend = prop.getProperty("render.abend");
		this.logStageDeal = prop.getProperty("log.stage.deal");
		this.logStageRecevie = prop.getProperty("log.stage.recevie");
		this.logLayerBroadcast = prop.getProperty("log.layer.broadcast");
		this.logLayerHanding = prop.getProperty("log.layer.handing");
		this.exPlayError = prop.getProperty("ex.play-error");
		this.exCastLayer = prop.getProperty("ex.cast-layer");
		this.exIllegalEvent = prop.getProperty("ex.illegal-event");
		this.exIllegalBoradcast = prop.getProperty("ex.illegal-broadcast");
		this.exElemHandingError = prop.getProperty("ex.elem-handing-error");
	}
	
	public static MessageTemplate inst() {
		if(msgTemp == null) {
			try {
				msgTemp = new MessageTemplate();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	public String getRenderAbend() {
		return renderAbend;
	}
}
