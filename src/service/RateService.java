package service;

import model.CurrencyRate;

import java.util.List;

public interface RateService {
    void saveExchangeRate(String date, CurrencyRate currency);
    boolean removeExchangeRate(String date, String currency);

    List<CurrencyRate> getList(String date);
}
