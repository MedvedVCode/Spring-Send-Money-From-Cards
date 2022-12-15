package ru.netology.sendmoney.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.sendmoney.exceptions.ErrorTransactionException;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.model.Card;
import ru.netology.sendmoney.model.transaction.Amount;
import ru.netology.sendmoney.model.transaction.TransactionInfo;
import ru.netology.sendmoney.repository.CardsRepository;
import ru.netology.sendmoney.repository.TransactionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
    @InjectMocks
    private TransferService transferService;
    @Mock
    private CardsRepository cardsRepository;
    @Mock
    private TransactionRepository transactionRepository;
    private Card rightCard = new Card("1111", "111", "01/25", 100000);

    @BeforeEach
    public void start() {
        transferService = new TransferService(cardsRepository, transactionRepository);
    }

    @Test
    void getCardToTestOk() {
        when(cardsRepository.getCardByNumber("1111")).thenReturn(
                Optional.of(rightCard)
        );
        var result = transferService.getCardTo("1111");
        assertEquals(result, rightCard);
    }

    @Test
    void getFromCardTestThrow() {
        var wrongCardNumber = "1212";
        when(cardsRepository.getCardByNumber(wrongCardNumber))
                .thenThrow(new InvalidDataFromClientException("Transaction failed. No card " + wrongCardNumber));
        assertThrows(InvalidDataFromClientException.class, () -> transferService.getCardTo(wrongCardNumber));
    }

    @Test
    void getCardFromTestOk(){
        when(cardsRepository.getCardByAllData("1111","111","01/25"))
                .thenReturn(Optional.of(rightCard));
        var result = transferService.getCardFrom("1111","111","01/25");
        assertEquals(result, rightCard);
    }

    @Test
    void getCardFromTestThrow(){
        when(cardsRepository.getCardByAllData("1212","111", "01/25"))
                .thenThrow(new InvalidDataFromClientException("Transaction failed. No card " + "1212"));
        assertThrows(InvalidDataFromClientException.class, () -> transferService.getCardFrom("1212","111","01/25"));
    }

    @Test
    void checkAndBlockCardBalanceTestOk(){
        var card = new Card("2222","222", "02/25",100000);
        var amount = new Amount("RUB",10000);
        amount.setCommissionPercent(1);
        var result = transferService.checkAndBlockCardBalance(card, amount);
        assertEquals(result,true);
    }

    @Test
    void checkAndBlockCardBalanceTestThrow() {
        var amount = new Amount("RUB",1000000);
        amount.setCommissionPercent(1);
        assertThrows(ErrorTransactionException.class, ()->transferService.checkAndBlockCardBalance(rightCard,amount));
    }

    @AfterEach
    public void finished() {
        transferService = null;
        cardsRepository = null;
        transactionRepository = null;
    }

}