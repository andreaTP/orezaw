package com.github.andreaTP.orezaw;

import java.util.Stack;

public class Processor {

    private final Stack<Integer> stack = new Stack<>();

    public void executeFunc(Func func, int[] params) {
        for (Instructions.Instruction instruction: func.getCode().getInstructions()) {
            if (instruction instanceof Instructions.LocalGet) {
                Instructions.LocalGet localGet = (Instructions.LocalGet) instruction;
                stack.push(params[localGet.getAddr()]);
            } else if (instruction instanceof Instructions.I32Add) {
                int a = stack.pop();
                int b = stack.pop();
                int result = a + b;
                stack.push(result);
            } else {
                throw new OrezawException("Operation not supported");
            }
        }
    }

    public int getResult() {
        return stack.pop();
    }
}
