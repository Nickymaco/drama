package org.drama.log;

public enum LoggingLevel {
	debug(2), info(4), warn(8), error(16);
	
	LoggingLevel(int levelValue) {
		this.levelValue=levelValue;
	}
	
	private int levelValue;
	
	public int getLevelValue() {
		return levelValue;
	}
	
	public void setLevelValue(int levelValue) {
		this.levelValue = levelValue;
	}
}
