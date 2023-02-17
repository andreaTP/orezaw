package com.github.andreaTP.orezaw;

public class Interpreter {

    // Invoke an exported function by its name with a list of i32 parameters
    public int invokeFunction(Module ast, String funcName, int[] params) {
        ExportDesc exportDesc = null;
        for (Export export: ast.getExports()) {
            if (export.getName().equals(funcName)) {
                exportDesc = export.getDesc();
            }
        }
        if (exportDesc == null) {
            throw new OrezawException("Export " + funcName + " not found in the module");
        }

        // Find the export descriptor for the export
        int index = exportDesc.getDesc();
        // Lookup the function from the export descriptor
        Func func = ast.getFuncs()[index];
        // Lookup the function type from the f_type index
        FuncType type = ast.getTypes()[func.getType()];
        // Check if the function type corresponds to the
        // number of provided parameters.
        // Note: We only support i32 here, as such we don't need to
        // check if the type matches.
        if (type.getParamTypes().length != params.length) {
            throw new OrezawException("Invalid argument number");
        };

        // Create a new processor that takes the function from the AST
        // and the list of parameters and executes the function.
        Processor processor = new Processor();
        processor.executeFunc(func, params);
        // Get the function result from the processor state
        // and return it
        return processor.getResult();
    }

}
