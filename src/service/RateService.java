package service;

import model.CurrencyRate;

import java.time.LocalDate;
import java.util.List;

public interface RateService {
    void saveExchangeRate(LocalDate date, CurrencyRate currency);
    boolean removeExchangeRate(LocalDate date, String currency);

    List<CurrencyRate> getList(LocalDate date);
}
