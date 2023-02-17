package com.github.andreaTP.orezaw;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterTest {

    @Test
    void interpreter() throws Exception {
        // Arrange
        Module module = new WASMInputStream(new ByteArrayInputStream(AddWasm.bytes)).parseWasm();
        Interpreter interpreter = new Interpreter();

        // Act
        int result = interpreter.invokeFunction(module, "add", new int[]{ 5, 6 });

        // Assert
        assertEquals(11, result);
    }

}
