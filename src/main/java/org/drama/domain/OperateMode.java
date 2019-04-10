package org.drama.domain;

public enum OperateMode {
    Create(2),
    Update(4),
    Retrieve(8),
    RetrieveList(16),
    Delete(32);

    private int code;

    private OperateMode(int code) {
        setCode(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
