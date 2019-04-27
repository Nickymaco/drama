package org.drama.core;

public interface BroadcastLisenter {
    HandingStatus getHandingStatus();

    void setHandingStatus(HandingStatus broadcastStatus);

    default void onElementHandingCompleted(Element element) {
    }

    default void onLayerBroadcastCompleted(Layer layer) {
    }
}