package service;

import model.CurrencyRate;
import model.LocalCurrency;
import repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public class CurrencyRateService implements RateService {

    private final CurrencyRepository repository;
    private List<CurrencyRate> currencyRateList;

    public CurrencyRateService(CurrencyRepository repository) {
        Objects.requireNonNull(repository);

        this.repository = repository;
    }

    /**
     * Extracts currencies from a file.
     * Replace specific currency if it exists and writes the list to a file.
     * */
    @Override
    public void saveExchangeRate(LocalDate date, CurrencyRate currency) {
        currencyRateList = repository.getExchangeRate(date);
        replaceCurrencyIfExist(currency, currencyRateList);
        repository.putExchangeRate(date, currencyRateList);
    }

    /**
     * Extracts currencies from a file. Deletes a specific currency,
     * if it exists, and writes the list to a file.
     * Returns true if the currency has been deleted, and false if the currency hasn't been deleted
     * Params: date - date of currency rate
     *         currencyToRemove - currency to be deleted
     */
    public boolean removeExchangeRate(LocalDate date, String currencyToRemove) {
        currencyRateList = repository.getExchangeRate(date);
        boolean isRemoved = removeIfExist(currencyToRemove, currencyRateList);
        repository.putExchangeRate(date, currencyRateList);

        return isRemoved;
    }

    /**
     * Returns a list of exchange rates
     * Params: date - date of currency rate
     */
    @Override
    public List<CurrencyRate> getList(LocalDate date) {
        currencyRateList = repository.getExchangeRate(date);
        return currencyRateList;
    }

/*    public BigDecimal exchangeCurrency(LocalDate date, BigDecimal amount, Currency fromCurrency, Currency toCurrency, LocalCurrency localCurrency) {
        currencyRateList = repository.getExchangeRate(date);
        CurrencyRate initialRate = null;
        CurrencyRate targetRate = null;

        if (fromCurrency.equals(localCurrency.getLocalCurrency())) {
            initialRate = localCurrency.getLocalCurrency();
        }

        if (toCurrency.equals(localCurrency.getLocalCurrency())) {
            targetRate = localCurrency.getLocalCurrency();
        }

        for (CurrencyRate currency : currencyRateList) {
            if (currency.getCurrency().equals(fromCurrency))
                initialRate = currency;
            if (currency.getCurrency().equals(toCurrency))
                targetRate = currency;
        }

        BigDecimal local = initialRate.getSellingRate().subtract(amount);
    }*/

    /**
     * The method will replace CurrencyRate if it exists.
     * Otherwise, it will add the currency exchange rate to the list.
     */
    private void replaceCurrencyIfExist(CurrencyRate currency, List<CurrencyRate> currencyRateList) {
        if (currencyRateList == null) {
            currencyRateList = new ArrayList<>();
            currencyRateList.add(currency);
        }

        int count = 0;
        for (CurrencyRate exchangeRate : currencyRateList) {
            if (exchangeRate.equals(currency)) {
                currencyRateList.set(count, currency);
                return;
            }
        }

        currencyRateList.add(currency);
    }

    /**
     * The method will remove the currency if it exists and return true.
     * Otherwise, return false.
     */
    private boolean removeIfExist(String currencyToRemove, List<CurrencyRate> currencyRateList) {
        int count = 0;

        for (CurrencyRate currencyRate : currencyRateList) {
            if (currencyRate.getCurrency().getCurrencyCode().equals(currencyToRemove)) {
                currencyRateList.remove(count);
                return true;
            }
            count++;
        }

        return false;
    }
}
