package org.drama.log;

public enum LoggingLevel {
    debug(2), info(4), warn(8), error(16);

    LoggingLevel(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int levelValue) {
        this.value = levelValue;
    }
}
