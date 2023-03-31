package model;

import java.math.BigDecimal;
import java.util.Currency;

public class CurrencyRate {
    /**
     * Currency sale rate
     */
    private final BigDecimal sellingRate;

    /**
     * The currency code in ISO-4217 format supported in java.util.Currency
     */
    private final Currency currency;

    /**
     * Currency purchase rate
     */
    private final BigDecimal purchaseRate;
    public CurrencyRate(BigDecimal sellingRate, Currency currency, BigDecimal purchaseRate) {
        if (sellingRate.compareTo(BigDecimal.ZERO) <= 0 || currency == null || purchaseRate.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Incorrect parameters of constructor.");

        this.sellingRate = sellingRate;
        this.currency = currency;
        this.purchaseRate = purchaseRate;
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
