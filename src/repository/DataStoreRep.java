package repository;

import model.CurrencyRate;
import model.LocalCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

public interface DataStoreRep {
    void putExchangeRate(LocalDate date, Map<String, CurrencyRate> currencyRate);
    Map<String, CurrencyRate> getCurrencyRateMap(LocalDate date);
    BigDecimal exchangeRate(LocalDate date, BigDecimal amount,
                 Currency fromCurrency, Currency targetCurrency, LocalCurrency localCurrency);
}
