package service;

import model.CurrencyRate;
import model.LocalCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface RateService {
    void saveExchangeRate(LocalDate date, Map<String, CurrencyRate> currency);
    boolean removeExchangeRate(LocalDate date, String currency);

    Map<String, CurrencyRate> getMap(LocalDate date);
    BigDecimal exchange(LocalDate date, BigDecimal amount, Currency fromCurrency, Currency toCurrency, LocalCurrency localCurrency);
}
