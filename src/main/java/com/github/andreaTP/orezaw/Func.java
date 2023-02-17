package com.github.andreaTP.orezaw;

public class Func {

    private final int type;
    private final Code code;

    public Func(int type, Code code) {
        this.type = type;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public Code getCode() {
        return code;
    }
}
