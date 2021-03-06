package org.drama.core;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.drama.log.LoggingFactory;
import org.drama.security.Signature;

public class DramaConfiguration implements Configuration, Signature {
    private static final long serialVersionUID = -5226125623860649004L;
    private LoggingFactory loggingFactory;
    private RegisterElementFactory registerElementFactory;
    private LayerFactory layerFactory;
    private UUID identity = UUID.randomUUID();
    private Serializable signer = this;
    private String[] eventPackage;

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
    public LoggingFactory getLoggingFactory() {
        return ObjectUtils.defaultIfNull(loggingFactory, new LoggingFactory.NullLogging());
    }

    public void setLoggingFactory(LoggingFactory loggingFactory) {
        this.loggingFactory = loggingFactory;
    }

    @Override
    public Signature getSignature() {
        return this;
    }

    @Override
    public String[] regeisterEventPackage() {
        return this.eventPackage;
    }

    public void setRegisterEventPackage(String[] eventPackage) {
        this.eventPackage = eventPackage;
    }

    @Override
    public UUID getIdentity() {
        return identity;
    }

    public void setIdentity(UUID identity) {
        this.identity = Objects.requireNonNull(identity);
    }

    @Override
    public Serializable getSigner() {
        return signer;
    }

    public void setSigner(Serializable signer) {
        this.signer = Objects.requireNonNull(signer);
    }

    @Override
    public boolean equals(Object o) {
        if (Objects.isNull(o) || !Objects.equals(getClass(), o.getClass())) {
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
