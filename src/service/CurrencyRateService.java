package service;

import model.CurrencyRate;
import repository.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurrencyRateService implements RateService {

    private final CurrencyRepository repository;
    private List<CurrencyRate> currencyRateList;

    public CurrencyRateService(CurrencyRepository repository) {
        Objects.requireNonNull(repository);

        this.repository = repository;
    }

    @Override
    public void saveExchangeRate(String date, CurrencyRate currency) {
        currencyRateList = repository.getExchangeRate(date);
        replaceCurrencyIfExist(currency, currencyRateList);
        repository.putExchangeRate(date, currencyRateList);
    }

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
}
