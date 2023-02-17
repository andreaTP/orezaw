package com.github.andreaTP.orezaw;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.github.andreaTP.orezaw.Leb128.readUnsignedLeb128;
import static com.github.andreaTP.orezaw.WASMConstants.*;

public class WASMInputStream extends InputStream {

    final InputStream inner;

    WASMInputStream(InputStream inner) {
        this.inner = inner;
    }

    public int readDword() {
        byte[] read = new byte[4];
        try {
            inner.read(read);
            return readUnsignedLeb128(read);
        } catch (IOException e) {
            throw new IllegalStateException("IOException while reading the stream", e);
        }
    }

    private byte readByte() {
        return readBytes(1)[0];
    }

    private byte[] readBytes(int size) {
        byte[] read = new byte[size];
        try {
            inner.read(read);
            return read;
        } catch (IOException e) {
            throw new IllegalStateException("IOException while reading the stream", e);
        }
    }

    private ValueType[] parseTypes() {
        int size = readByte();
        ValueType[] types = new ValueType[size];
        for (int i = 0; i < size; i++) {
            types[i] = ValueType.fromByte(readByte());
        }
        return types;
    }

    public FuncType[] readTypeSection() {
        if (readByte() != SECTION_TYPE) {
            throw new OrezawException("Invalid section code");
        }
        byte size = readByte(); // ignored?
        int numTypes = readByte();

        FuncType[] types = new FuncType[numTypes];
        for (int i = 0; i < numTypes; i++) {
            byte func = readByte();
            if (func != FUNC_TYPE) {
                throw new OrezawException("Unsupported type " + func);
            }

            ValueType[] paramTypes = parseTypes();
            ValueType[] resultsTypes = parseTypes();

            types[i] = new FuncType(paramTypes, resultsTypes);
        }
        return types;
    }

    public int[] readFuncSection() {
        if (readByte() != SECTION_FUNC) {
            throw new OrezawException("Invalid section code");
        }
        byte size = readByte(); // ignored?
        int num = readByte();

        int[] indices = new int[num];
        for (int i = 0; i < num; i++) {
            indices[i] = readByte();
        }
        return indices;
    }

    public Export[] readExportSection() {
        if (readByte() != SECTION_EXPORT) {
            throw new OrezawException("Invalid section code");
        }
        byte size = readByte(); // ignored?
        int num = readByte();

        Export[] exports = new Export[num];
        for (int i = 0; i < num; i++) {
            int length = readByte();

            String name = new String(readBytes(length), StandardCharsets.UTF_8);

            int zero = readByte();
            ExportDesc type = new ExportDesc(readByte());
            exports[i] = new Export(name, type);
        }
        return exports;
    }

    private Instructions.Instruction readInstruction() {
        switch (readByte()) {
            case 0x20:
                return new Instructions.LocalGet(readByte());
            case 0x6a:
                return new Instructions.I32Add();
            case 0x0b:
                return new Instructions.End();
            default:
                throw new OrezawException("Invalid instruction code");
        }
    }

    public Code[] readCodeSection() {
        if (readByte() != SECTION_CODE) {
            throw new OrezawException("Invalid section code");
        }

        byte size = readByte(); // ignored?
        int num = readByte();

        Code[] codes = new Code[num];
        for (int i = 0; i < num; i++) {
            byte inner_size = readByte(); // ignored?

            ValueType[] locals = parseTypes();
            List<Instructions.Instruction> instructions = new ArrayList<>();
            while (true) {
                Instructions.Instruction instruction = readInstruction();
                if (instruction instanceof Instructions.End) {
                    break;
                } else {
                    instructions.add(instruction);
                }
            }

            codes[i] = new Code(locals, instructions.toArray(new Instructions.Instruction[0]));
        }
        return codes;
    }

    public void checkMatching(byte[] match) {
        try {
            if (inner.available() >= match.length) {
                byte[] read = new byte[match.length];
                int length = inner.read(read);
                if (length != match.length) {
                    throw new OrezawException("Read length is not correct " + read.length);
                }
                for (int i = 0; i < match.length; i++) {
                    if (read[i] != match[i]) {
                        throw new OrezawException("No matching at position " + i);
                    }
                }
            } else {
                throw new OrezawException("Not enough bytes available to check init");
            }
        } catch (IOException e) {
            throw new IllegalStateException("IOException while reading the stream", e);
        }
    }

    public Module parseWasm() {
        // check headers
        checkMatching(MAGIC);
        checkMatching(VERSION);

        // parse the sections
        FuncType[] types = readTypeSection();
        int[] funcs = readFuncSection();
        Export[] exports = readExportSection();
        Code[] codes = readCodeSection();

        List<Func> functions = new ArrayList<>(funcs.length);
        for (int i = 0; i < funcs.length; i++) {
            functions.add(new Func(funcs[i], codes[i]));
        }

        return new Module(types, exports, functions.toArray(new Func[0]));
    }

    @Override
    public int read() throws IOException {
        if (inner.available() > 0) {
            return inner.read();
        } else {
            throw new OrezawException("No available data on the underlying stream");
        }
    }

}
