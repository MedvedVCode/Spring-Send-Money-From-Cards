package ru.netology.sendmoney.repository;

import org.springframework.stereotype.Repository;
import ru.netology.sendmoney.model.Card;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CardsRepository {
    private final int INITIAL_BALANCE = 100000;
    private ConcurrentHashMap<String, Card> cardRepository = new ConcurrentHashMap<>();

    public CardsRepository() {
        Card card = new Card("1111111111111111", "111", "01/25", INITIAL_BALANCE);
        cardRepository.put(card.getCardNumber(), card);
        card = new Card("2222222222222222", "222", "02/25", INITIAL_BALANCE);
        cardRepository.put(card.getCardNumber(), card);
    }

    public Optional<Card> getCardByNumber(String cardNumber) {
        return Optional.ofNullable(cardRepository.getOrDefault(cardNumber, null));
    }

    public Optional<Card> getCardByAllData(String cardNumber, String cardCVV, String cardValidTill) {
        Optional<Card> optionalCard = getCardByNumber(cardNumber);
        if (optionalCard.isPresent()) {
            Card searchCard = new Card(cardNumber, cardCVV, cardValidTill, 0);
            optionalCard.filter(x -> x.equals(searchCard));
        }
        return optionalCard;
    }
}
