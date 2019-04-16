package org.drama.core;

class StageRender implements Render {
	private static final long serialVersionUID = 4805058851279820776L;
	private int code;
	private Object model;
	private String message;

	public StageRender() {
		this(Render.SUCCESS, null);
	}

	public StageRender(int code, Object model) {
		this(code, model, "");
	}

	public StageRender(int code, Object model, String message) {
		this.code = code;
		this.model = model;
		this.message = message;
	}

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public Object getModel() {
		return this.model;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
