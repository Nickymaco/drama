package org.drama.core;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.drama.log.LoggingFactory;

import java.util.Objects;
import java.util.UUID;

public class DramaConfiguration implements Configuration, Signature {
	private static final long serialVersionUID = -5226125623860649004L;
	private LoggingFactory loggingFactory;
	private RegisterElementFactory registerElementFactory;
	private RegisterEventFactory registerEventFactory;
	private LayerFactory layerFactory;
	private UUID identity = UUID.randomUUID();
	private Object signer = this;

	@Override
	public Kernel getKernel() {
		return DramaKernel.getInstance(this);
	}

	@Override
	public BroadcastLisenter getBroadcastLisenter() {
		return new DramaBroadcastLisenter();
	}

	@Override
	public LayerFactory getLayerFactory() {
		return layerFactory;
	}

	public void setLayerFactory(LayerFactory layerFactory) {
		this.layerFactory = layerFactory;
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

	@Override
	public Render defaultErrorRender() {
		return new StageRender(Render.FAILURE, null, Render.ERROR_MSG);
	}

	@Override
	public Signature getSignature() {
		return this;
	}

	@Override
	public UUID getIdentity() {
		return identity;
	}

	public void setIdentity(UUID identity) {
		this.identity = Objects.requireNonNull(identity);
	}

	@Override
	public Object getSigner() {
		return signer;
	}

	public void setSigner(Object signer) {
		this.signer = Objects.requireNonNull(signer);
	}

	@Override
	public boolean equals(Object o) {
		if(Objects.isNull(o) || !Objects.equals(getClass(), o.getClass())) {
			return false;
		}

		DramaConfiguration that = (DramaConfiguration) o;

		return Objects.equals(identity, that.identity);
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(identity);
		return hashCodeBuilder.toHashCode();
	}
}
