package com.github.andreaTP.orezaw;

public class Code {
    private final ValueType[] locals;
    private final Instructions.Instruction[] instructions;

    public Code(ValueType[] locals, Instructions.Instruction[] instructions) {
        this.locals = locals;
        this.instructions = instructions;
    }

    public ValueType[] getLocals() {
        return locals;
    }

    public Instructions.Instruction[] getInstructions() {
        return instructions;
    }
}
