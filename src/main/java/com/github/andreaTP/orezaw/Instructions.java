package com.github.andreaTP.orezaw;

public interface Instructions {
    interface Instruction { }

    public static class LocalGet implements Instruction {
        private final int addr;

        public LocalGet(int addr) {
            this.addr = addr;
        }

        public int getAddr() {
            return addr;
        }
    };

    public static class I32Add implements Instruction { };
    public static class End implements Instruction { };
}
