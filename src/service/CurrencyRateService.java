package service;

import model.CurrencyRate;
import model.LocalCurrency;
import repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;

public class CurrencyRateService implements RateService {

    private final CurrencyRepository repository;
    private Map<String, CurrencyRate> currencyRateMap;

    public CurrencyRateService(CurrencyRepository repository) {
        Objects.requireNonNull(repository);

        this.repository = repository;
    }

    /**
     * Adds currency to a file, if currency exists, it'll be overwritten
     */
    @Override
    public void saveExchangeRate(LocalDate date, Map<String, CurrencyRate> currencyRate) {
        repository.putExchangeRate(date, currencyRate);
    }

    /**
     * Extracts currencies from a file. Deletes a specific currency,
     * if it exists, and writes the list to a file.
     * Returns true if the currency has been deleted, and false if the currency hasn't been deleted
     * Params: date - date of currency rate
     *         currencyToRemove - currency to be deleted
     */
    public boolean removeExchangeRate(LocalDate date, String currencyToRemove) {
        currencyRateMap = repository.getCurrencyRateMap(date);

        CurrencyRate removedCurrency = currencyRateMap.remove(currencyToRemove);
        repository.putExchangeRate(date, currencyRateMap);

        return removedCurrency != null;
    }

    /**
     * Returns a list of exchange rates
     * Params: date - date of currency rate
     */
    @Override
    public Map<String, CurrencyRate> getMap(LocalDate date) {
        currencyRateMap = repository.getCurrencyRateMap(date);
        return currencyRateMap;
    }

    @Override
    public BigDecimal exchange(LocalDate date, BigDecimal amount, Currency fromCurrency, Currency toCurrency, LocalCurrency localCurrency) {
        return repository.exchangeRate(date, amount, fromCurrency, toCurrency, localCurrency);
    }
}
