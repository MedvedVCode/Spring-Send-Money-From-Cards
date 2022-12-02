package ru.netology.sendmoney;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import ru.netology.sendmoney.model.Card;
import ru.netology.sendmoney.model.operation.ConfirmOperation;
import ru.netology.sendmoney.model.transaction.TransactionInfo;
import ru.netology.sendmoney.repository.TransactionRepository;
import ru.netology.sendmoney.service.ConfirmService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfirmServiceTest {
    public static TransactionRepository transactionRepository;
    public ConfirmService confirmService;
    public ConfirmOperation confirmTrue;
    public ConfirmOperation confirmFalse;

    @BeforeEach
    public void init() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        Mockito.when(transactionRepository.getTransactionByOperationId("123456"))
                .thenReturn(Optional.of(new TransactionInfo(
                                new Card("1234567890", "999", "01/25", 100000),
                                new Card("0987654321", "111", "02/25", 100000),
                                10000,
                                1000,
                                "0000"
                        ))
                );

        confirmTrue = new ConfirmOperation();
        confirmTrue.setOperationId("123456");
        confirmTrue.setCode("0000");

        confirmFalse = new ConfirmOperation();
        confirmFalse.setOperationId("654321");
        confirmFalse.setCode("1111");

        confirmService = new ConfirmService(transactionRepository);
    }

    @Test
    public void checkConfirmOperationOkTest() {

        ConfirmOperation result = confirmService.checkConfirmOperation(confirmTrue);
        assertEquals(result,confirmTrue);
    }

    @Test
    public void getTransactionFromConfirmOperationTest(){
        TransactionInfo result = confirmService.getTransactionFromConfirmOperation(confirmTrue);
        assertEquals(result, transactionRepository.getTransactionByOperationId(confirmTrue.getOperationId()));
    }

    @AfterAll
    public void finalized() {
        transactionRepository = null;
        confirmService = null;
        confirmTrue = null;
        confirmFalse = null;
    }
}
