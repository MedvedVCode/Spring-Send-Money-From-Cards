package ru.netology.sendmoney.model.operation;

public class ConfirmOperation {
    private String operationId;
    private String code;

    public ConfirmOperation() {
    }

    @Override
    public String toString() {
        return "ConfirmOperation{" +
                "operationId=" + operationId +
                ", code=" + code +
                '}';
    }

    public String getOperationId() {
        return operationId;
    }

    public String getCode() {
        return code;
    }
}
