package model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class CurrencyRate {
    /**
     * The currency code in ISO-4217 format supported in java.util.Currency
     */
    private final Currency currency;

    /**
     * Currency sale rate
     */
    private final BigDecimal sellingRate;

    /**
     * Currency purchase rate
     */
    private final BigDecimal purchaseRate;
    public CurrencyRate(Currency currency, BigDecimal purchaseRate, BigDecimal sellingRate) {
        if (currency == null || sellingRate.compareTo(BigDecimal.ZERO) <= 0 || purchaseRate.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Incorrect parameters of constructor.");

        this.currency = currency;
        this.purchaseRate = purchaseRate;
        this.sellingRate = sellingRate;
    }
    @Override
    public boolean equals(Object obj) {
        CurrencyRate anotherCurrency = (CurrencyRate) obj;
        if (this == anotherCurrency) return true;

        return this.getCurrency().equals(anotherCurrency.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellingRate, currency, purchaseRate);
    }

    public BigDecimal getSellingRate() {
        return sellingRate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getPurchaseRate() {
        return purchaseRate;
    }
}
