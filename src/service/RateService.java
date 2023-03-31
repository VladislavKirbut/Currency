package service;

import model.CurrencyRate;

public interface RateService {
    void saveExchangeRate(String date, CurrencyRate currency);
    boolean removeExchangeRate(String date, String currency);
}
