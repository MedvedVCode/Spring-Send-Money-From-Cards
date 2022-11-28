package ru.netology.sendmoney.model;

import java.util.Objects;

public class Amount {
    private String currency;
    private int value;
    private int comissionPercent = 1;

    public Amount() {
    }

    public Amount(String currency, int value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public int getValue() {
        return value;
    }

    public int getComissionPercent() {
        return comissionPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return getValue() == amount.getValue() && Objects.equals(getCurrency(), amount.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrency(), getValue());
    }

    @Override
    public String toString() {
        return "Amount{" +
                "currency='" + currency + '\'' +
                ", value=" + value +
                '}';
    }
}
