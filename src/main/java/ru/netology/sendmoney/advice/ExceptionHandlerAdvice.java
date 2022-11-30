package ru.netology.sendmoney.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.sendmoney.exceptions.ErrorTransactionException;
import ru.netology.sendmoney.exceptions.InvalidDataFromClientException;
import ru.netology.sendmoney.model.CardErrorAnswer;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(InvalidDataFromClientException.class)
    public ResponseEntity<CardErrorAnswer> idfceHandler(InvalidDataFromClientException e) {
        return new ResponseEntity<>(new CardErrorAnswer(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransactionException.class)
    public ResponseEntity<CardErrorAnswer> eteHandler(ErrorTransactionException e) {
        return new ResponseEntity<>(new CardErrorAnswer(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
