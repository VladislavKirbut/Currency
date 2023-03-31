package service;

import model.CurrencyRate;
import repository.CurrencyRepository;

import java.util.Objects;

public class CurrencyService {

    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        Objects.requireNonNull(repository);

        this.repository = repository;
    }

    public void saveExchangeRate(String date, CurrencyRate currency) {
        repository.putExchangeRate(date, currency);
    }
}
