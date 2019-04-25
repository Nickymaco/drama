package org.drama.core;

import org.apache.commons.lang3.ObjectUtils;
import org.drama.log.LoggingFactory;

public class DramaConfiguration implements Configuration {
	private static final long serialVersionUID = -5226125623860649004L;
	private LoggingFactory loggingFactory;
	private Kernel kernel = new DramaKernel();
	private RegisterElementFactory registerElementFactory;
	private RegisterEventFactory registerEventFactory;

	@Override
	public Kernel getKernel() {
		return kernel;
	}

	public void setKernel(Kernel kernel) {
		this.kernel = kernel;
	}

	@Override
	public BroadcastLisenter getBroadcastLisenter() {
		return new DramaBroadcastLisenter();
	}

	@Override
	public LayerFactory getLayerFactory() {
		return null;
	}

	@Override
	public RegisterElementFactory getRegisterElementFactory() {
		return registerElementFactory;
	}

	public void setRegisterElementFactory(RegisterElementFactory registerElementFactory) {
		this.registerElementFactory = registerElementFactory;
	}

	@Override
	public RegisterEventFactory getRegisterEventFactory() {
		return registerEventFactory;
	}

	public void setRegisterEventFactory(RegisterEventFactory registerEventFactory) {
		this.registerEventFactory = registerEventFactory;
	}

	@Override
	public LoggingFactory getLoggingFactory() {
		return ObjectUtils.defaultIfNull(loggingFactory, LoggingFactory.NULL);
	}

	public void setLoggingFactory(LoggingFactory loggingFactory) {
		this.loggingFactory = loggingFactory;
	}

}
