package ru.netology.sendmoney.repository;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.sendmoney.model.Card;
import ru.netology.sendmoney.model.transaction.TransactionInfo;
import ru.netology.sendmoney.repository.TransactionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionRepositoryTest {
    public TransactionRepository transactionRepository;

    @BeforeEach
    public void init() {
        transactionRepository = new TransactionRepository();
        transactionRepository.addTransaction("123456", new TransactionInfo(
                new Card("1234567890", "999", "01/25", 100000),
                new Card("0987654321", "111", "02/25", 100000),
                10000,
                1000,
                "0000"
        ));
    }

    @Test
    public void getTransactionByOperationIdTestOk() {
        var result = Optional.of(new TransactionInfo(
                new Card("1234567890", "999", "01/25", 100000),
                new Card("0987654321", "111", "02/25", 100000),
                10000,
                1000,
                "0000"
        ));
        assertEquals(result, transactionRepository.getTransactionByOperationId("123456"));
    }

    @Test
    public void getTransactionByOperationIdTestNull() {
        assertNull(transactionRepository.getTransactionByOperationId("11111"));
    }

    @AfterEach
    public void finalized() {
        transactionRepository = null;
    }

}
