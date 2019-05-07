package org.drama.core;

import static org.drama.core.LayerContants.DEFAULT_NAME;
import static org.drama.core.LayerContants.DEFAULT_PRIORITY;
import static org.drama.core.LayerContants.DEFAULT_UUID;

public enum DramaLayerDescriptor implements LayerDescriptor {
    Drama(DEFAULT_NAME, DEFAULT_UUID, DEFAULT_PRIORITY, new String[]{});

    private String name;
    private String UUID;
    private int priority;
    private String[] excludeEvent;

    DramaLayerDescriptor(String name, String UUID, int priority, String[] exculdeEvent) {
        this.name = name;
        this.UUID = UUID;
        this.priority = priority;
        this.excludeEvent = exculdeEvent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getUUID() {
        return UUID;
    }

    @Override
    public boolean getDisabled() {
        return false;
    }

    @Override
    public String[] getExculdeEvent() {
        return excludeEvent;
    }
}
