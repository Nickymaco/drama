package org.drama.core;

import org.drama.event.Event;

public interface PlayLisenter {
	/**
	 * 占位符，防止抛空指针异常
	 */
	static final PlayLisenter NULL = new PlayLisenter() {
		@Override
		public void onCompletedPlay(Event event) {
		}
	};
	/**
	 * 在stage play 前触发
	 * 
	 * @param event
	 * @return 返回{@code true}则退出广播
	 */
	default boolean onBeforePlay(Event event) {
		return false;
	}
	/**
	 * 在stage play后触发
	 * 
	 * @param event
	 */
	void onCompletedPlay(Event event);
}
