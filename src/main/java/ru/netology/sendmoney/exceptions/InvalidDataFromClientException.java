package ru.netology.sendmoney.exceptions;

public class InvalidDataFromClientException extends RuntimeException {

    public InvalidDataFromClientException(String message) {
        super(message);
    }
}
