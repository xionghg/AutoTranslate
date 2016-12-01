package com.example.translateStrings.bean;

public class StringLine {

	public static final String DEFAULT_KEY = "UnknownString";

	private String key;

	private String value;

	private int lineNumber;

	private boolean isTranslated;

	public StringLine() {
	}

	public StringLine(String key, String value, int lineNumber, boolean isTranslated) {
		this.key = key;
		this.value = value;
		this.lineNumber = lineNumber;
		this.isTranslated = isTranslated;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public boolean isTranslated() {
		return isTranslated;
	}

	public void setTranslated(boolean isTranslated) {
		this.isTranslated = isTranslated;
	}
}
