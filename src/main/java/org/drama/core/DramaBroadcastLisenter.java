package org.drama.core;

public class DramaBroadcastLisenter implements BroadcastLisenter {
	private HandingStatus handingStatus = HandingStatus.Transmit;

	@Override
	public HandingStatus getHandingStatus() {
		return handingStatus;
	}

	@Override
	public void setHandingStatus(HandingStatus broadcastStatus) {
		this.handingStatus = broadcastStatus;
	}
}
