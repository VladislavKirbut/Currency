package repository;

import model.CurrencyRate;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public interface DataStoreRep {
    void putExchangeRate(LocalDate date, List<CurrencyRate> currencyRateList);
    List<CurrencyRate> getExchangeRate(LocalDate date);
}
