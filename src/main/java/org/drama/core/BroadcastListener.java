package org.drama.core;

public interface BroadcastListener {
    BroadcastListener Default = new BroadcastListener() {
        private BroadcastStatus broadcastStatus;

        @Override
        public BroadcastStatus getBroadcastStatus() {
            return broadcastStatus;
        }

        @Override
        public void setBroadcastStatus(BroadcastStatus broadcastStatus) {
            this.broadcastStatus = broadcastStatus;
        }
    };

    BroadcastStatus getBroadcastStatus();

    void setBroadcastStatus(BroadcastStatus broadcastStatus);

    default void onElementHandingCompleted(Element element) {
    }

    default void onLayerBroadcastCompleted(Layer layer) {
    }
}