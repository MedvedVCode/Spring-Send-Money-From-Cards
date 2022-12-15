package ru.netology.sendmoney;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.sendmoney.model.Card;
import ru.netology.sendmoney.repository.CardsRepository;
import ru.netology.sendmoney.repository.TransactionRepository;
import ru.netology.sendmoney.service.ConfirmService;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMoneyApplicationTests {

    public CardsRepository cards;
    public TransactionRepository transactions;

    public ConfirmService confirmService;

    @Before
    void prepare() {

    }

    @Test
    void CardTest() {
        cards = new CardsRepository();
        Optional<Card> card = cards.getCardByNumber("1111");
        Assertions.assertNull(card.orElse(null));
    }

    @Test
    void repostitoryTest() {
        transactions = new TransactionRepository();
        Assertions.assertNull(transactions.getTransactionByOperationId("11111").orElse(null));
    }
}
