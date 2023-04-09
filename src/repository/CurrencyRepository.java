package repository;

import config.RepositoryConfiguration;
import exceptions.CurrencyRateException;
import model.CurrencyRate;
import model.LocalCurrency;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class CurrencyRepository implements DataStoreRep {

    /** Config contains properties of CurrencyRepository */
    private final RepositoryConfiguration config;

    public CurrencyRepository(RepositoryConfiguration config) {
        Objects.requireNonNull(config);

        this.config = config;
    }

    /**
     * Adds a file to a specific directory if this file doesn't exist.
     * Params: date - date of exchange rate
     * Throws: UncheckedIOException
     */
    private Path createFullPath(LocalDate date) {
        Path filePath;
        try {
            String fileName = date + ".csv";
            filePath = config.getPathToDataStore().resolve(fileName);

            if (Files.notExists(filePath)) Files.createFile(filePath);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return filePath;
    }

    /**
     * Adds currency rate map to a file with a specific date.
     * Params: date - date of exchange rate
     *         currencyRateMap - map of currency rates on a specific date
     * Throws: UncheckedIOException
     */
    private void putExchangeRateMap(LocalDate date, Map<String, CurrencyRate> currencyRateMap) {

        List<String> csvLines = new ArrayList<>();

        for (Map.Entry<String, CurrencyRate> entry : currencyRateMap.entrySet()) {
            CurrencyRate rate = entry.getValue();
            String csvLine = String.join(", ",
                String.valueOf(rate.getCurrency()),
                String.valueOf(rate.getPurchaseRate()),
                String.valueOf(rate.getSellingRate())
            );
            csvLines.add(csvLine);
        }

        try {
            Files.write(createFullPath(date), csvLines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    /**
     * Returns Map of CurrencyRate from file.
     * Params: date - date of exchange rate
     * Throws: UncheckedIOException
     */
    @Override
    public Map<String, CurrencyRate> getCurrencyRateMap(LocalDate date) {
        List<String> csvLines;

        try {
            csvLines = Files.readAllLines(createFullPath(date), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }

        Map<String, CurrencyRate> currencyRateMap = new HashMap<>();

        for (String csvLine : csvLines) {
            String[] parts = csvLine.split(", ");
            currencyRateMap.put(parts[0], new CurrencyRate(
                             Currency.getInstance(parts[0]),
                             new BigDecimal(parts[1]),
                             new BigDecimal(parts[2])
            ));
        }

        return currencyRateMap;
    }

    /**
     * Adds currency rate to map, if rate already exists, it'll be overwritten
     * Params: date - date of exchange rate
     *         currencyRate - rate on a specific date
     */
    @Override
    public void putExchangeRate(LocalDate date, Map<String, CurrencyRate> currencyRate) {

        Map<String, CurrencyRate> currencyRateMap = getCurrencyRateMap(date);
        currencyRateMap.putAll(currencyRate);

        putExchangeRateMap(date, currencyRateMap);
    }

    @Override
    public BigDecimal exchangeRate(LocalDate date, BigDecimal amount,
                                   Currency fromCurrency, Currency toCurrency, LocalCurrency localCurrency) {

        if (amount.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;

        if (fromCurrency.equals(toCurrency))
            return amount;

        Map<String, CurrencyRate> exchangeRateMap = getCurrencyRateMap(date);
        BigDecimal toCurrencyPurchaseRate = null;
        BigDecimal fromCurrencySellingRate = null;

        if (fromCurrency.equals(localCurrency.getLocalCurrency())) {
            toCurrencyPurchaseRate = exchangeRateMap.get(toCurrency.toString()).getPurchaseRate();
            return amount.divide(toCurrencyPurchaseRate, 10, RoundingMode.HALF_UP);
        }

        CurrencyRate startCurrency = exchangeRateMap.get(fromCurrency.toString());

        if (startCurrency != null) {
            fromCurrencySellingRate = startCurrency.getSellingRate();
        } else {
            throw new CurrencyRateException("Данные о курсе валюты отсутствуют");
        }

        BigDecimal baseCurrency = amount.multiply(fromCurrencySellingRate);

        if (toCurrency.equals(localCurrency.getLocalCurrency()))
            return baseCurrency;

        toCurrencyPurchaseRate = exchangeRateMap.get(toCurrency.toString()).getPurchaseRate();

        return baseCurrency.divide(toCurrencyPurchaseRate, 10, RoundingMode.HALF_UP);
    }
}
