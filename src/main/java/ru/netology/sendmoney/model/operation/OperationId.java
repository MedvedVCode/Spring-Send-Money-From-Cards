package ru.netology.sendmoney.model.operation;

import java.util.UUID;

public class OperationId {
    @Override
    public String toString() {
        return "OperationId{" +
                "operationId='" + operationId + '\'' +
                '}';
    }

    public String getOperationId() {
        return operationId;
    }

    private String operationId;

    public OperationId() {
        this.operationId = UUID.randomUUID().toString();
    }
}
