package org.drama.core;

public class DramaBroadcastLisenter implements BroadcastLisenter {
	private ThreadLocal<HandingStatus> handingStatus = new ThreadLocal<>();
	
	public DramaBroadcastLisenter() {
		handingStatus.set(HandingStatus.Transmit);
	}

	@Override
	public HandingStatus getHandingStatus() {
		return handingStatus.get();
	}

	@Override
	public void setHandingStatus(HandingStatus handingStatus) {
		this.handingStatus.set(handingStatus);
	}
}
