package org.drama.core;

public interface BroadcastListener {
    BroadcastStatus getBroadcastStatus();

    void setBroadcastStatus(BroadcastStatus broadcastStatus);

    default void onElementHandingCompleted(Element element) {
    }

    default void onLayerBroadcastCompleted(Layer layer) {
    }
}