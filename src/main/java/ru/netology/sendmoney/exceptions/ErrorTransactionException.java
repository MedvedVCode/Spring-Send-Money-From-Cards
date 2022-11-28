package ru.netology.sendmoney.exceptions;

public class ErrorTransactionException extends RuntimeException {

    public ErrorTransactionException(String message) {
        super(message);
    }
}
