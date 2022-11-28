package ru.netology.sendmoney.model;

import java.util.Random;

public class CardErrorAnswer {
    private String message;
    private int id;

    public CardErrorAnswer(String message) {
        this.message = message;
        this.id = new Random().nextInt(0,1000);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }
}
