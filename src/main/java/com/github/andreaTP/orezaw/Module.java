package com.github.andreaTP.orezaw;

public class Module {

    private final FuncType[] types;
    private final Export[] exports;
    private final Func[] funcs;

    public Module(FuncType[] types, Export[] exports, Func[] funcs) {
        this.types = types;
        this.exports = exports;
        this.funcs = funcs;
    }

    public FuncType[] getTypes() {
        return types;
    }

    public Export[] getExports() {
        return exports;
    }

    public Func[] getFuncs() {
        return funcs;
    }
}
