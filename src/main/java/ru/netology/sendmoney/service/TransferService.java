package ru.netology.sendmoney.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.netology.sendmoney.controller.GetDataFromClientController;
import ru.netology.sendmoney.exceptions.ErrorTransactionException;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.model.Amount;
import ru.netology.sendmoney.model.Card;
import ru.netology.sendmoney.model.ConfirmOperation;
import ru.netology.sendmoney.model.Transaction;
import ru.netology.sendmoney.repository.CardsRepository;

import java.util.Optional;

@Service
public class TransferService {
    private static Logger logger = LoggerFactory.getLogger(GetDataFromClientController.class);
    private CardsRepository cardsRepository;

    public TransferService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    public ConfirmOperation doTransaction(Transaction transaction) {
        Card cardFrom = getCardFrom(transaction.getCardFromNumber(), transaction.getCardFromCVV(), transaction.getCardFromValidTill());
        Card cardTo = getCardTo(transaction.getCardToNumber());
        return checkTransaction(cardFrom, cardTo, transaction.getAmount());
    }

    private ConfirmOperation checkTransaction(Card cardFrom, Card cardTo, Amount amount) {
        int transferValueWithComission = amount.getValue() + amount.getValue() * amount.getComissionPercent() / 100;
        cardFrom.getBalance().accumulateAndGet(transferValueWithComission, (x, y) ->
        {
            if (x - y >= 0) {
                return x - y;
            } else {
                logger.info("Not enouth money on balance " + cardFrom.getCardNumber());
                throw new ErrorTransactionException("Not enouth money on balance " + cardFrom.getCardNumber());
            }
        });
        cardTo.getBalance().accumulateAndGet(amount.getValue(), (x, y) -> (x + y));
        logger.info(String.format("Transaction is done! %s balance %d, %s balance %d",
                cardFrom.getCardNumber(), cardFrom.getBalance().get(), cardTo.getCardNumber(), cardTo.getBalance().get()));

        return new ConfirmOperation("88888888");
    }

    private Card getCardFrom(String cardFromNumber, String cardFromCVV, String cardFromValidTill) {
        Optional<Card> optionalCard = cardsRepository.getCardByAllData(cardFromNumber, cardFromCVV, cardFromValidTill);
        if (optionalCard.isEmpty()) {
            throw new InvalidDataFromClientException("No card for transfer with number " + cardFromNumber);
        }
        return optionalCard.get();
    }

    public Card getCardTo(String cardNumber) {
        Optional<Card> optionalCard = cardsRepository.getCardByNumber(cardNumber);
        if (optionalCard.isEmpty()) {
            throw new InvalidDataFromClientException("No card to transfer with number " + cardNumber);
        }
        return optionalCard.get();
    }
}
