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
     * Gets currencies from a file. Deletes a specific currency,
     * if it exists, and writes the map to a file.
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
     * Returns a map of exchange rates
     * Params: date - date of currency rate
     */
    @Override
    public Map<String, CurrencyRate> getMap(LocalDate date) {
        currencyRateMap = repository.getCurrencyRateMap(date);
        return currencyRateMap;
    }

    /**
    * Calls exchangeCurrency method from the repository, passes parameters and performs the exchange
    * Param: date - date of exchange rate
    *         amount - amount of money in "fromCurrency" currency
    *         fromCurrency - the currency for exchange
    *         tragetCurrency - the currency for purchase
    *         localCurrency - basic currency 
    */
    @Override
    public BigDecimal exchange(LocalDate date, BigDecimal amount, Currency fromCurrency, Currency targetCurrency, LocalCurrency localCurrency) {
        return repository.exchangeCurrency(date, amount, fromCurrency, targetCurrency, localCurrency);
    }
}
