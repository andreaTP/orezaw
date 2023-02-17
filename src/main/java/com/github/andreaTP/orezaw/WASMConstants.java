package com.github.andreaTP.orezaw;

public class WASMConstants {

    private WASMConstants() {}

    public static final byte[] MAGIC = new byte[] { 0x00, 0x61, 0x73, 0x6d }; // \0asm
    public static final byte[] VERSION = new byte[] { 0x01, 0x00, 0x00, 0x00 }; // 1

    public static final byte SECTION_TYPE = 0x01;
    public static final byte SECTION_FUNC = 0x03;
    public static final byte SECTION_EXPORT = 0x07;
    public static final byte SECTION_CODE = 0x0a;

    public static final byte FUNC_TYPE = 0x60;

}
