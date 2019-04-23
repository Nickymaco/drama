package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.OccurredException;
import org.drama.log.LoggingFactory;

/**
 * 事件舞台
 */
public interface Stage {
	/**
	 * 内核
	 * @return
	 */
	Kernel getKernel();
	/**
	 * 逻辑处理层工厂
	 * @param layerFactory
	 */
	void setLayerFactory(LayerFactory layerFactory);
	/**
	 * 事件传播监听器
	 * @return
	 */
	BroadcastLisenter getBroadcastLisenter();
	/**
	 * 设置时间传播监听器
	 * @param lisenter
	 */
	void setBroadcastLisenter(BroadcastLisenter lisenter);
    /**
     * 注册元素
     * @param element
     */
    void setRegisterElementFactory(RegisterElementFactory registerElementFactory);
    /**
     * 注册事件
     * @param events
     */
    void setRegisterEventFactory(RegisterEventFactory registerEventFactory);
    /**
     * 获取逻辑处理层，这里应该返回只读集合
     * @return
     */
    ImmutableSet<Layer> getLayers();
    /**
     * 添加日志
     * @param loggingFactory
     */
    void setLoggingFactory(LoggingFactory loggingFactory);
    /**
     * 演出
     */
    Render play(Event... events) throws OccurredException;
    /**
     * 演出
     * @param lisenter
     * @param events
     * @return
     * @throws OccurredException
     */
    Render play(PlayLisenter lisenter, Event... events) throws OccurredException;
    /**
     * 启动
     */
    void setup() throws OccurredException;
    /**
     * 默认异常输出
     * @return
     */
    static Render defaultErrorRender() {
    	return new StageRender(Render.FAILURE, null, Render.ErrorMsg);
    }
}
