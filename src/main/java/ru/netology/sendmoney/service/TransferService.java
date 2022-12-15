package ru.netology.sendmoney.service;

import org.springframework.stereotype.Service;
import ru.netology.sendmoney.exceptions.ErrorTransactionException;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.logger.LoggerTransaction;
import ru.netology.sendmoney.model.*;
import ru.netology.sendmoney.model.operation.OperationId;
import ru.netology.sendmoney.model.transaction.Amount;
import ru.netology.sendmoney.model.transaction.Transaction;
import ru.netology.sendmoney.model.transaction.TransactionInfo;
import ru.netology.sendmoney.repository.CardsRepository;
import ru.netology.sendmoney.repository.TransactionRepository;

import java.util.Optional;

@Service
public class TransferService {
    private final int INITIAL_PERCENT = 1;
    private final String INITIAL_CODE = "0000";
    private static LoggerTransaction logger = LoggerTransaction.getLogger();
    private CardsRepository cardsRepository;
    private TransactionRepository transactionRepository;

    public TransferService(CardsRepository cardsRepository, TransactionRepository transactionRepository) {
        this.cardsRepository = cardsRepository;
        this.transactionRepository = transactionRepository;
    }

    public OperationId checkTransfer(Transaction transaction) {

        transaction.getAmount().setCommissionPercent(INITIAL_PERCENT);
        logger.log(String.format("  %s", transaction));

        var cardFrom = getCardFrom(transaction.getCardFromNumber(), transaction.getCardFromCVV(), transaction.getCardFromValidTill());
        var cardTo = getCardTo(transaction.getCardToNumber());
        checkAndBlockCardBalance(cardFrom, transaction.getAmount());

        return addTransactionRepository(cardFrom, cardTo, transaction.getAmount());
    }

    public OperationId addTransactionRepository(Card cardFrom, Card cardTo, Amount amount) {
        var operation = new OperationId();
        var transactionInfo = new TransactionInfo(
                cardFrom,
                cardTo,
                amount.getValue(),
                amount.getCommission(),
                INITIAL_CODE
        );
        transactionRepository.addTransaction(operation.getOperationId(), transactionInfo);
        logger.log(String.format("  %s", operation));
        logger.log("  Transaction is possible! Money blocked! Waiting for confirmation!");
        return operation;
    }

    public boolean checkAndBlockCardBalance(Card cardFrom, Amount amount) {
        cardFrom.getBalance().accumulateAndGet(
                amount.getValue() + amount.getCommission(),
                (x, y) ->
                {
                    if (x - y >= 0) {
                        return x - y;
                    } else {
                        logger.log(String.format(" > Transaction failed. Not enough money on balance %s\n", cardFrom.getCardNumber()));
                        throw new ErrorTransactionException("Not enough money on balance " + cardFrom.getCardNumber());
                    }
                });
        return true;
    }

    public Card getCardFrom(String cardFromNumber, String cardFromCVV, String cardFromValidTill) {
        Optional<Card> optionalCard = cardsRepository.getCardByAllData(cardFromNumber, cardFromCVV, cardFromValidTill);
        if (optionalCard.isEmpty()) {
            logger.log(String.format(" > Transaction failed. No card %s\n", cardFromNumber));
            throw new InvalidDataFromClientException("Transaction failed. No card " + cardFromNumber);
        }
        return optionalCard.get();
    }

    public Card getCardTo(String cardNumber) {
        Optional<Card> optionalCard = cardsRepository.getCardByNumber(cardNumber);
        if (optionalCard.isEmpty()) {
            logger.log(String.format(" > Transaction failed. No card %s\n", cardNumber));
            throw new InvalidDataFromClientException("Transaction failed. No card " + cardNumber);
        }
        return optionalCard.get();
    }
}
