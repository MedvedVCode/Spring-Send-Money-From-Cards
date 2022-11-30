package ru.netology.sendmoney.service;

import org.springframework.stereotype.Service;
import ru.netology.sendmoney.exceptions.ErrorTransactionException;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.logger.LoggerTransaction;
import ru.netology.sendmoney.model.operation.ConfirmOperation;
import ru.netology.sendmoney.model.transaction.Transaction;
import ru.netology.sendmoney.repository.CardsRepository;
import ru.netology.sendmoney.repository.TransactionRepository;

@Service
public class ConfirmService {
    private static LoggerTransaction logger = LoggerTransaction.getLogger();
    private TransactionRepository transactionRepository;
    private CardsRepository cardsRepository;

    public ConfirmService(TransactionRepository transactionRepository, CardsRepository cardsRepository) {
        this.transactionRepository = transactionRepository;
        this.cardsRepository = cardsRepository;
    }

    public ConfirmOperation checkConfirmOperation(ConfirmOperation confirmOperation) {
        logger.log(confirmOperation.toString());
        var transaction = getTransactionFromConfirmOperation(confirmOperation);
        checkCodeOperation(confirmOperation, transaction);
        var cardFrom = cardsRepository.getCardByNumber(transaction.getCardFromNumber()).get();
        var cardTo = cardsRepository.getCardByNumber(transaction.getCardToNumber()).get();
        cardTo.getBalance().accumulateAndGet(transaction.getAmount().getValue(), (x, y) -> x + y);
        logger.log("> Transaction is OK!");
        logger.log("> From " + cardFrom);
        logger.log("> To " + cardTo);
        return confirmOperation;
    }

    private boolean checkCodeOperation(ConfirmOperation confirmOperation, Transaction transaction) {
        if (!transaction.getCode().equals(confirmOperation.getCode())) {
            logger.log("Transaction failed! Error customer message confirmation code");
            doRollBackTransaction(transaction);
            throw new ErrorTransactionException("Error customer message confirmation code");
        }
        return true;
    }

    private void doRollBackTransaction(Transaction transaction) {
        var cardFrom = cardsRepository
                .getCardByNumber(transaction.getCardFromNumber()).get();
        cardFrom.getBalance().accumulateAndGet(
                transaction.getAmount().getValue() + transaction.getAmount().getComission(),
                (x, y) -> x + y);
    }

    private Transaction getTransactionFromConfirmOperation(ConfirmOperation confirmOperation) {

        var transactionOptional = transactionRepository
                .getTransactionByOperationId(confirmOperation.getOperationId());
        if (transactionOptional.isEmpty()) {
            logger.log("Transaction failed! Error input data in confirm code");
            throw new InvalidDataFromClientException("Error input data in confirm code");
        }
        return transactionOptional.get();
    }
}
