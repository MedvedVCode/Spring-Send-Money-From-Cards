package ru.netology.sendmoney.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Card {
    private String cardNumber;
    private String cardCVV;
    private String cardValidTill;
    private AtomicInteger balance;

    public Card(String cardNumber, String cardCVV, String cardValidTill, int balance) {
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.cardValidTill = cardValidTill;
        this.balance = new AtomicInteger(balance);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public String getCardValidTill() {
        return cardValidTill;
    }

    public AtomicInteger getBalance() {
        return balance;
    }

    public void setBalance(AtomicInteger balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(getCardNumber(), card.getCardNumber()) && Objects.equals(getCardCVV(), card.getCardCVV()) && Objects.equals(getCardValidTill(), card.getCardValidTill());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardNumber(), getCardCVV(), getCardValidTill());
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardCVV='" + cardCVV + '\'' +
                ", cardValidTill='" + cardValidTill + '\'' +
                ", balance=" + balance +
                '}';
    }
}
