package org.drama.core;

class StageRender implements Render {
	private static final long serialVersionUID = 4805058851279820776L;
	private int code;
    private boolean success;
    private Object model;
    private String message;

    public StageRender() {
    	code = Render.SUCCESS;
    	success = true;
    }

    public StageRender(int code, boolean success, Object model) {
        this.code = code;
        this.success = success;
        this.model = model;
    }
    
    public StageRender(int code, boolean success, Object model, String message) {
        this.code = code;
        this.success = success;
        this.model = model;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public boolean getSuccess() {
        return this.success;
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

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
