package ru.netology.sendmoney.model.transaction;

import java.util.Objects;

public class Transaction {

    private String cardFromNumber;
    private String cardFromCVV;
    private String cardFromValidTill;
    private Amount amount;
    private String cardToNumber;
    private String code;

    public String getCardToNumber() {
        return cardToNumber;
    }

    public Transaction(String cardFromNumber, String cardFromCVV, String cardFromValidTill, Amount amount, String cardToNumber) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromCVV = cardFromCVV;
        this.cardFromValidTill = cardFromValidTill;
        this.amount = amount;
        this.cardToNumber = cardToNumber;
    }

    public Transaction() {
    }

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    public String getCardFromValidTill() {
        return cardFromValidTill;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "cardFromNumber='" + cardFromNumber + '\'' +
                ", cardFromCVV='" + cardFromCVV + '\'' +
                ", cardFromValidTill='" + cardFromValidTill + '\'' +
                ", amount=" + amount +
                ", cardToNumber='" + cardToNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(getCardFromNumber(), that.getCardFromNumber()) && Objects.equals(getCardFromCVV(), that.getCardFromCVV()) && Objects.equals(getCardFromValidTill(), that.getCardFromValidTill()) && Objects.equals(getAmount(), that.getAmount()) && Objects.equals(getCardToNumber(), that.getCardToNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardFromNumber(), getCardFromCVV(), getCardFromValidTill(), getAmount(), getCardToNumber());
    }
}
