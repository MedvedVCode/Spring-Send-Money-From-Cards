package ru.netology.sendmoney.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.netology.sendmoney.model.Card;
import ru.netology.sendmoney.model.operation.ConfirmOperation;
import ru.netology.sendmoney.model.transaction.TransactionInfo;
import ru.netology.sendmoney.repository.TransactionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ConfirmServiceTest {
    @InjectMocks
    public ConfirmService confirmService;
    @Mock
    public TransactionRepository transactionRepository;
    public ConfirmOperation confirmTrue;
    public ConfirmOperation confirmFalse;

    @Before
    public void init() {
        //MockitoAnnotations.initMocks(this);
        //transactionRepository = Mockito.mock(TransactionRepository.class);
        when(transactionRepository.getTransactionByOperationId("123456"))
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
        assertEquals(result, confirmTrue);
    }

    @Test
    public void getTransactionFromConfirmOperationTest() {
        TransactionInfo result = confirmService.getTransactionFromConfirmOperation(confirmTrue);
        assertEquals(result, transactionRepository.getTransactionByOperationId(confirmTrue.getOperationId()));
    }

    @After
    public void finalized() {
        transactionRepository = null;
        confirmService = null;
        confirmTrue = null;
        confirmFalse = null;
    }
}
