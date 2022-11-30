package ru.netology.sendmoney.model.transaction;

import ru.netology.sendmoney.model.Card;

public class TransactionInfo {
    private Card cardFrom;
    private Card cardTo;
    private int value;
    private int commission;
    private String code;

    public TransactionInfo(Card cardFrom, Card cardTo, int value, int commission, String code) {
        this.cardFrom = cardFrom;
        this.cardTo = cardTo;
        this.value = value;
        this.commission = commission;
        this.code = code;
    }

    public Card getCardFrom() {
        return cardFrom;
    }

    public void setCardFrom(Card cardFrom) {
        this.cardFrom = cardFrom;
    }

    public Card getCardTo() {
        return cardTo;
    }

    public void setCardTo(Card cardTo) {
        this.cardTo = cardTo;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
