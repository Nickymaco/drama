package org.drama.core;

import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.DramaException;
import org.drama.log.LoggingFactory;
import org.drama.log.template.LayerLoggingTemplate;
import org.drama.log.template.LoggingTemplateFactory;

/**
 * 默认逻辑处理层，如果没有指定逻辑处理层，舞台默认使用它进行构建
 */
public class DramaLayer implements Layer {
    private Configuration configuration;
    private LayerLoggingTemplate logging;
    private ImmutableSet<Element> elementSet;
    private Kernel kernel;

    protected ImmutableSet<Element> getElements() {
        if (CollectionUtils.isEmpty(elementSet)) {
            elementSet = kernel.getElements(this);
        }
        return elementSet;
    }

    @Override
    public void broadcast(Event event, BroadcastListener broadcasetListener) {
        if (Objects.isNull(event)) {
            throw DramaException.illegalBroadcastEvent(this, null);
        }

        // 设置当前逻辑处理层
        event.getContext().setCurrentLayer(this);

        kernel.notifyHandler(this, event, l -> {
            // 打印日志
            getLogging().broadcast(l.getName(), event);
        }, e -> {
            // 打印完成日志
            getLogging().handingElement(e.getSimpleName());

            if (Objects.nonNull(broadcasetListener)) {
                broadcasetListener.setBroadcastStatus(e.getHandingStatus());
                broadcasetListener.onElementHandingCompleted(e.getInvocator());
            }
        });

        if (Objects.nonNull(broadcasetListener)) {
            broadcasetListener.onLayerBroadcastCompleted(this);
        }
    }

    protected LayerLoggingTemplate getLogging() {
        if (Objects.isNull(logging)) {
            logging = LoggingTemplateFactory.getLayerLoggingTemplate(
                    ObjectUtils.defaultIfNull(configuration.getLoggingFactory(), new LoggingFactory.NullLogging()));
        }
        return logging;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        if (Objects.equals(Objects.requireNonNull(configuration), this.configuration)) {
            return;
        }

        this.configuration = configuration;
        this.kernel = KernelFactory.getInstance().getKernel(configuration.getSignature());
    }
}
