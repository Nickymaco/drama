package org.drama.core;

import static org.drama.text.Symbol.EMPTY;

import java.util.HashMap;
import java.util.Map;

public class DramaRender implements Render {
    private static final long serialVersionUID = 4805058851279820776L;
    private int code;
    private Map<String, Object> model;
    private String message;

    public DramaRender() {
        this(Render.SUCCESS, new HashMap<>());
    }

    public DramaRender(int code, Map<String, Object> model) {
        this(code, model, EMPTY);
    }

    public DramaRender(int code, Map<String, Object> model, String message) {
        this.code = code;
        this.model = model;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public Map<String, Object> getModel() {
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
    public void setModel(Map<String, Object> model) {
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
