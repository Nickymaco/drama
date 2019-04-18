package org.drama.core;

import org.drama.event.Event;

/**
 * 逻辑处理层事件句柄
 */
public interface Element extends Comparable<Element> {
    /**
     * 优先级
     * @return
     */
    int getPriority();
    /**
     * 告知逻辑处理层是否继续往下执行
     * @return
     */
    default Broken cancelable(){ return Broken.None; }

    /**
     * 触发事件关联行为
     * @param event
     */
    void handing(Event event);
    
	@Override
	default int compareTo(Element o) {
		if(o == null) {
            return 1;
        } else if (o.getPriority() == getPriority()) {
            return 0;
        } else {
            return o.getPriority() > getPriority() ? -1 : 1;
        }
	}
}
