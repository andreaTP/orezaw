package com.github.andreaTP.orezaw;

// Adapted from https://github.com/facebook/buck/blob/master/third-party/java/dx/src/com/android/dex/Leb128.java

public class Leb128 {

    /**
     * Reads an unsigned integer from {@code in}.
     */
    static int readUnsignedLeb128(byte[] in) {
        int result = 0;
        int cur;
        int count = 0;

        do {
            cur = in[count] & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);

        if ((cur & 0x80) == 0x80) {
            throw new OrezawException("Illegal binary data");
        }

        return result;
    }

}
