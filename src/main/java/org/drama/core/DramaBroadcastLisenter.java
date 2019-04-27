package org.drama.core;

import java.util.Objects;

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
        if (Objects.equals(this.handingStatus, handingStatus)) {
            return;
        }
        this.handingStatus.set(handingStatus);
    }
}
