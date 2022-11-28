package ru.netology.sendmoney.service;

import org.springframework.stereotype.Service;
import ru.netology.sendmoney.exceptions.ErrorTransactionException;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.model.ConfirmOperation;

@Service
public class ConfirmService {
    private final int LENGTH_CODE_CONFIRM = 4;
    private final String CODE_PATTERN = "0000";

    public ConfirmOperation checkConfirmOperation(ConfirmOperation confirmOperation) {
        if (confirmOperation.getCode().length() != LENGTH_CODE_CONFIRM) {
            throw new InvalidDataFromClientException("Error input data in confirm code");
        }
        if(!confirmOperation.getCode().equals(CODE_PATTERN)){
            throw new ErrorTransactionException("Error customer message confirmation code");
        }
        return confirmOperation;
    }
}
