package com.github.andreaTP.orezaw;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.github.andreaTP.orezaw.WASMConstants.MAGIC;
import static org.junit.jupiter.api.Assertions.*;

public class WASMInputStreamTest {

    @Test
    void basicWASMInputStreamFunctionality() throws Exception {
        byte[] ba = new byte[]{ 1, 2, 3 };
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        WASMInputStream wis = new WASMInputStream(bais);

        assertEquals(1, wis.read());
        assertEquals(2, wis.read());
        assertEquals(3, wis.read());
        assertThrows(OrezawException.class,() -> wis.read());
    }

    @Test
    void checkMagic() throws Exception {
        byte[] ba = new byte[]{ 0x00, 0x61, 0x73, 0x6d };
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        WASMInputStream wis = new WASMInputStream(bais);

        assertDoesNotThrow(() -> wis.checkMatching(MAGIC));
    }

    @Test
    void negativeCheckMagic() throws Exception {
        byte[] ba = new byte[]{ 0x00, 0x61, 0x73, 0x6c };
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        WASMInputStream wis = new WASMInputStream(bais);

        assertThrows(OrezawException.class, () -> wis.checkMatching(MAGIC));
    }

    @Test
    void getUInt() throws Exception {
        byte[] ba = new byte[]{ 0x01, 0x00, 0x00, 0x00 };
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        WASMInputStream wis = new WASMInputStream(bais);

        assertEquals(1, wis.readDword());
    }

    @Test
    void parseFuncTypeSection() throws Exception {
        // Arrange
        byte[] ba = new byte[]{
                // section "Type"
                0x01, // section code
                0x07, // section size
                0x01, // num types
                // type 0
                0x60, // func
                0x02, // num params
                0x7f, // i32
                0x7f, // i32
                0x01, // num results
                0x7f, // i32
        };
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        WASMInputStream wis = new WASMInputStream(bais);

        // Act
        FuncType[] funcTypes = wis.readTypeSection();

        // Assert
        assertEquals(1, funcTypes.length);
        assertEquals(2, funcTypes[0].getParamTypes().length);
        assertEquals(1, funcTypes[0].getResultTypes().length);
        assertEquals(ValueType.I32, funcTypes[0].getParamTypes()[0]);
        assertEquals(ValueType.I32, funcTypes[0].getParamTypes()[1]);
        assertEquals(ValueType.I32, funcTypes[0].getResultTypes()[0]);
    }

    @Test
    void parseFuncIndicesSection() throws Exception {
        // Arrange
        byte[] ba = new byte[]{
                // section "Function" (3)
                0x03, // section code
                0x02, // section size
                0x01, // num functions
                0x00, // function 0 signature index
        };
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        WASMInputStream wis = new WASMInputStream(bais);

        // Act
        int[] indices = wis.readFuncSection();

        // Assert
        assertEquals(1, indices.length);
        assertEquals(0, indices[0]);
    }

    @Test
    void parseExportSection() throws Exception {
        // Arrange
        byte[] ba = new byte[]{
                // section "Export" (7)
                0x07, // section export
                0x07, // section size
                0x01, // num exports
                0x03, // string length
                // "add" export name
                0x61, // a
                0x64, // d
                0x64, // d
                0x00, // 0
                // export kind
                0x00, // export func index
        };
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        WASMInputStream wis = new WASMInputStream(bais);

        // Act
        Export[] exports = wis.readExportSection();

        // Assert
        assertEquals(1, exports.length);
        assertEquals("add", exports[0].getName());
        assertEquals(0, exports[0].getDesc().getDesc());
    }

    @Test
    void parseCodeSection() throws Exception {
        // Arrange
        byte[] ba = new byte[]{
                // section "Code" (10)
                0x0a, // section code
                0x09, // section size
                0x01, // num function
                // function body 0
                0x07, // func body size
                0x00, // local decl count
                0x20, // local.get
                0x00, // local index
                0x20, // local.get
                0x01, // local index
                0x6a, // i32.add
                0x0b, // end
        };
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        WASMInputStream wis = new WASMInputStream(bais);

        // Act
        Code[] codes = wis.readCodeSection();

        // Assert
        assertEquals(1, codes.length);
        assertEquals(0, codes[0].getLocals().length);
        assertEquals(3, codes[0].getInstructions().length);
    }

    @Test
    void parseWasm() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(AddWasm.bytes);
        WASMInputStream wis = new WASMInputStream(bais);

        assertDoesNotThrow(() -> wis.parseWasm());
    }

}
