package ru.netology.sendmoney.repository;

import org.springframework.stereotype.Repository;
import ru.netology.sendmoney.model.operation.OperationId;
import ru.netology.sendmoney.model.transaction.Transaction;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class TransactionRepository {
    private ConcurrentHashMap<String, Transaction> transactionRepository;

    public TransactionRepository() {
        this.transactionRepository = new ConcurrentHashMap<>();
    }

    public Optional<Transaction> getTransactionByOperationId(String operationId) {
        return Optional.ofNullable(transactionRepository.getOrDefault(operationId, null));
    }

    public void addTransaction(String operationId, Transaction transaction){
        transactionRepository.put(operationId, transaction);
    }
}
