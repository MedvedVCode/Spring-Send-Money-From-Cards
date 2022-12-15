package ru.netology.sendmoney.service;

import org.springframework.stereotype.Service;
import ru.netology.sendmoney.exceptions.ErrorTransactionException;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.logger.LoggerTransaction;
import ru.netology.sendmoney.model.operation.ConfirmOperation;
import ru.netology.sendmoney.model.transaction.TransactionInfo;
import ru.netology.sendmoney.repository.TransactionRepository;

@Service
public class ConfirmService {
    private static LoggerTransaction logger = LoggerTransaction.getLogger();
    private TransactionRepository transactionRepository;

    public ConfirmService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public ConfirmOperation checkConfirmOperation(ConfirmOperation confirmOperation) {
        logger.log(String.format("  %s", confirmOperation));

        var transactionInfo = getTransactionFromConfirmOperation(confirmOperation);
        checkCodeOperation(confirmOperation, transactionInfo);

        doTransaction(transactionInfo);

        return confirmOperation;
    }

    public void doTransaction(TransactionInfo transactionInfo) {
        var cardFrom = transactionInfo.getCardFrom();
        var cardTo = transactionInfo.getCardTo();
        cardTo.getBalance().accumulateAndGet(transactionInfo.getValue(), (x, y) -> x + y);
        logger.log(" > Transaction is OK!");
        logger.log(" > From " + cardFrom);
        logger.log(String.format(" > To %s\n", cardTo));
    }

    public boolean checkCodeOperation(ConfirmOperation confirmOperation, TransactionInfo transactionInfo) {
        if (!transactionInfo.getCode().equals(confirmOperation.getCode())) {
            logger.log("> Transaction failed! Error customer message confirmation code\n");
            doRollBackTransaction(transactionInfo);
            throw new ErrorTransactionException("Error customer message confirmation code");
        }
        return true;
    }

    public void doRollBackTransaction(TransactionInfo transactionInfo) {
        var cardFrom = transactionInfo.getCardFrom();
        cardFrom.getBalance().accumulateAndGet(
                transactionInfo.getValue() + transactionInfo.getCommission(),
                (x, y) -> x + y);
    }

    public TransactionInfo getTransactionFromConfirmOperation(ConfirmOperation confirmOperation) {
        var transactionOptional = transactionRepository
                .getTransactionByOperationId(confirmOperation.getOperationId());
        if (transactionOptional.isEmpty()) {
            logger.log("> Transaction failed! Error input data in confirm code\n");
            throw new InvalidDataFromClientException("Error input data in confirm code");
        }
        return transactionOptional.get();
    }
}
