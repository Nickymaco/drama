package org.drama.core;

import org.drama.event.Event;

public class LayerContants {
    public static final class NullLayer implements Layer {
        @Override
        public void broadcast(Event event, BroadcastListener broadcasetListener) {
        }

        @Override
        public Configuration getConfiguration() {
            return null;
        }

        @Override
        public void setConfiguration(Configuration configuration) {
        }
    }

    public static final String DEFAULT_NAME = "Drama";
    public static final String DEFAULT_UUID = "A66A23C6-1A62-4B53-AD80-6DDB58D900D";
    public static final int DEFAULT_PRIORITY = 3290;
}
