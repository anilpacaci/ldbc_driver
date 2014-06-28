package com.ldbc.driver.validation;

import com.ldbc.driver.Operation;

public class ValidationParam<OPERATION extends Operation<RESULT>, RESULT> {
    private final OPERATION operation;
    private final RESULT operationResult;

    public ValidationParam(OPERATION operation, RESULT operationResult) {
        this.operation = operation;
        this.operationResult = operationResult;
    }

    public OPERATION operation() {
        return operation;
    }

    public RESULT operationResult() {
        return operationResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidationParam that = (ValidationParam) o;

        if (operation != null ? !operation.equals(that.operation) : that.operation != null) return false;
        if (operationResult != null ? !operationResult.equals(that.operationResult) : that.operationResult != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operation != null ? operation.hashCode() : 0;
        result = 31 * result + (operationResult != null ? operationResult.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ValidationParam{" +
                "operation=" + operation +
                ", operationResult=" + operationResult +
                '}';
    }
}
