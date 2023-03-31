package repository;

import model.CurrencyRate;

import java.nio.file.Path;
import java.util.List;

public interface DataStoreRep {
    void putExchangeRate(String date, List<CurrencyRate> currencyRateList);
    List<CurrencyRate> getExchangeRate(String date);
}
