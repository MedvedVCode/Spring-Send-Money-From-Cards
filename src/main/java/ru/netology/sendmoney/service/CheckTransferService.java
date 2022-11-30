package ru.netology.sendmoney.service;

import org.springframework.stereotype.Service;
import ru.netology.sendmoney.exceptions.ErrorTransactionException;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.logger.LoggerTransaction;
import ru.netology.sendmoney.model.*;
import ru.netology.sendmoney.model.operation.OperationId;
import ru.netology.sendmoney.model.transaction.Transaction;
import ru.netology.sendmoney.repository.CardsRepository;
import ru.netology.sendmoney.repository.TransactionRepository;

import java.util.Optional;

@Service
public class CheckTransferService {
    private final int INITIAL_PERCENT = 1;
    private final String INITIAL_CODE = "0000";
    private static LoggerTransaction logger = LoggerTransaction.getLogger();
    private CardsRepository cardsRepository;
    private TransactionRepository transactionRepository;

    public CheckTransferService(CardsRepository cardsRepository, TransactionRepository transactionRepository) {
        this.cardsRepository = cardsRepository;
        this.transactionRepository = transactionRepository;
    }

    public OperationId checkCardBalance(Transaction transaction) {
        transaction.getAmount().setComissionPercent(INITIAL_PERCENT);
        logger.log(transaction.toString());
        var cardFrom = getCardFrom(transaction.getCardFromNumber(), transaction.getCardFromCVV(), transaction.getCardFromValidTill());
        getCardTo(transaction.getCardToNumber());
        return checkCardBalance(cardFrom, transaction);
    }

    private OperationId checkCardBalance(Card cardFrom, Transaction transaction) {
        var transferValueWithComission = transaction.getAmount().getValue()
                + transaction.getAmount().getComission();
        cardFrom.getBalance().accumulateAndGet(transferValueWithComission, (x, y) ->
        {
            if (x - y >= 0) {
                return x - y;
            } else {
                logger.log("Transaction failed. Not enough money on balance " + cardFrom.getCardNumber());
                throw new ErrorTransactionException("Not enough money on balance " + cardFrom.getCardNumber());
            }
        });
        var operation = new OperationId();
        transaction.setCode(INITIAL_CODE);
        transactionRepository.addTransaction(operation.getOperationId(), transaction);
        logger.log(operation.toString());
        logger.log("Transaction is possible! Money blocked! Waiting for confirmation!");
        return operation;
    }

    private Card getCardFrom(String cardFromNumber, String cardFromCVV, String cardFromValidTill) {
        Optional<Card> optionalCard = cardsRepository.getCardByAllData(cardFromNumber, cardFromCVV, cardFromValidTill);
        if (optionalCard.isEmpty()) {
            logger.log("Transaction failed. No card " + cardFromNumber);
            throw new InvalidDataFromClientException("Transaction failed. No card " + cardFromNumber);
        }
        return optionalCard.get();
    }

    public Card getCardTo(String cardNumber) {
        Optional<Card> optionalCard = cardsRepository.getCardByNumber(cardNumber);
        if (optionalCard.isEmpty()) {
            logger.log("Transaction failed. No card " + cardNumber);
            throw new InvalidDataFromClientException("Transaction failed. No card " + cardNumber);
        }
        return optionalCard.get();
    }
}
