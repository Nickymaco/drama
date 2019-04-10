package com.john.drama.core;

import java.io.Serializable;

/**
 * 逻辑处理层广播结果
 */
public class BroadcastResult implements Serializable {
	private static final long serialVersionUID = 5915072910428039365L;
	private BroadcastTracer status;

    public BroadcastResult() {
    }

	public BroadcastResult(BroadcastTracer status) {
		this.status = status;
	}

	public BroadcastTracer getStatus() {
		return status;
	}

	public void setStatus(BroadcastTracer status) {
		this.status = status;
	}
}
