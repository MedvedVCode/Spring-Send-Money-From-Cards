package ru.netology.sendmoney.repository;

import org.springframework.stereotype.Repository;
import ru.netology.sendmoney.model.transaction.TransactionInfo;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {
    private ConcurrentHashMap<String, TransactionInfo> transactionRepository;

    public TransactionRepository() {
        this.transactionRepository = new ConcurrentHashMap<>();
    }

    public Optional<TransactionInfo> getTransactionByOperationId(String operationId) {
        return Optional.ofNullable(transactionRepository.getOrDefault(operationId, null));
    }

    public void addTransaction(String operationId, TransactionInfo transaction) {
        transactionRepository.put(operationId, transaction);
    }
}
