package com.github.andreaTP.orezaw;

public enum ValueType {
    I32,
    I64;

    static ValueType fromByte(byte value) {
        switch(value) {
            case 0x7f:
                return I32;
            case 0x7e:
                return I64;
            default:
                throw new OrezawException("Invalid Value Type");
        }
    }
}
