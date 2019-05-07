package org.drama.core;

public class DramaRender implements Render {
    private static final long serialVersionUID = 4805058851279820776L;
    private int code;
    private Object model;
    private String message;

    public DramaRender() {
        this(Render.SUCCESS, null);
    }

    public DramaRender(int code, Object model) {
        this(code, model, "");
    }

    public DramaRender(int code, Object model, String message) {
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

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public void setModel(Object model) {
        this.model = model;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

	@Override
	public void close() throws Exception {
	}
}
