package com.github.andreaTP.orezaw;

public class FuncType {

    private final ValueType[] paramTypes;
    private final ValueType[] resultTypes;

    public FuncType(ValueType[] paramTypes, ValueType[] resultTypes) {
        this.paramTypes = paramTypes;
        this.resultTypes = resultTypes;
    }

    public ValueType[] getParamTypes() {
        return paramTypes;
    }

    public ValueType[] getResultTypes() {
        return resultTypes;
    }

}
