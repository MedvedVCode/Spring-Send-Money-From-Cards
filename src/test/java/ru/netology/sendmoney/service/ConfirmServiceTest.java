package ru.netology.sendmoney.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.model.Card;
import ru.netology.sendmoney.model.operation.ConfirmOperation;
import ru.netology.sendmoney.model.transaction.TransactionInfo;
import ru.netology.sendmoney.repository.TransactionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmServiceTest {
    @InjectMocks
    private ConfirmService confirmService;
    @Mock
    private TransactionRepository transactionRepository;
    private Card cardFrom = new Card("1111", "111", "01/25", 100000);
    private Card cardTo = new Card("2222", "222", "02/25", 100000);
    private TransactionInfo transactionInfo;
    private ConfirmOperation confirmOperation;

    @BeforeEach
    public void init() {
        confirmOperation = new ConfirmOperation();
        transactionInfo = new TransactionInfo(cardFrom,
                cardTo,
                20000,
                200,
                "0000");

        confirmService = new ConfirmService(transactionRepository);
    }

    @Test
    void checkConfirmOperation() {
        confirmOperation.setCode("0000");
        confirmOperation.setOperationId("123456");
        when(transactionRepository.getTransactionByOperationId("123456"))
                .thenReturn(Optional.of(
                        new TransactionInfo(
                                new Card("1111", "111", "01/25", 100000),
                                new Card("2222", "222", "02/25", 100000),
                                20000, 200, "0000"
                        )
                ));
        var result = confirmService.checkConfirmOperation(confirmOperation);
        assertEquals(result,confirmOperation);
    }

    @Test
    void doTransaction() {
        var confirmMock = mock(ConfirmService.class);
        doNothing().when(confirmMock).doTransaction(transactionInfo);
        confirmMock.doTransaction(transactionInfo);
        verify(confirmMock, times(1)).doTransaction(transactionInfo);
    }

    @Test
    void checkCodeOperationOk() {
        confirmOperation.setCode("0000");
        confirmOperation.setOperationId("123456");
        var result = confirmService.checkCodeOperation(confirmOperation,transactionInfo);
        assertEquals(true, result);
    }

    @Test
    void doRollBackTransaction() {
        var confirmMock = mock(ConfirmService.class);
        doNothing().when(confirmMock).doRollBackTransaction(isA(TransactionInfo.class));
        confirmMock.doRollBackTransaction(transactionInfo);
        verify(confirmMock, times(1)).doRollBackTransaction(transactionInfo);
    }

    @Test
    void getTransactionFromConfirmOperationTestOK() {
        when(transactionRepository.getTransactionByOperationId("123456"))
                .thenReturn(Optional.of(
                        new TransactionInfo(
                                new Card("1111", "111", "01/25", 100000),
                                new Card("2222", "222", "02/25", 100000),
                                20000, 200, "0000"
                        )
                ));
        confirmOperation.setCode("0000");
        confirmOperation.setOperationId("123456");
        var result = confirmService.getTransactionFromConfirmOperation(confirmOperation);
        assertEquals(result, transactionInfo);
    }

    @Test
    void getTransactionFromConfirmOperationTestThrow() {
        when(transactionRepository.getTransactionByOperationId("121212"))
                .thenThrow(new InvalidDataFromClientException("Error input data in confirm code"));
        confirmOperation.setOperationId("121212");
        confirmOperation.setCode("0000");
        assertThrows(InvalidDataFromClientException.class, () -> confirmService.getTransactionFromConfirmOperation(confirmOperation));
    }

    @AfterEach
    public void finished() {
        confirmService = null;
        transactionRepository = null;
        confirmOperation = null;
    }
}