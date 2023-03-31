package repository;

import config.RepositoryConfiguration;
import model.CurrencyRate;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CurrencyRepository implements DataStoreRep {

    /**
     * Config contains properties of CurrencyRepository
     */
    private final RepositoryConfiguration config;

    public CurrencyRepository(RepositoryConfiguration config) {
        Objects.requireNonNull(config);

        this.config = config;
    }

    /**
     * createFullPath method adds a file to a specific directory if this file doesn't exist.
     */
    private Path createFullPath(String date) {
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
     * Adds currency to a file with a specific date
     * If this currency exists, it'll be overwritten
     */
    @Override
    public void putExchangeRate(String date, List<CurrencyRate> currencyRateList) {

        List<String> csvLines = new ArrayList<>();

        for (CurrencyRate currency : currencyRateList) {
            String csvLine = String.join(", ",
                    String.valueOf(currency.getCurrency()),
                    String.valueOf(currency.getSellingRate()),
                    String.valueOf(currency.getPurchaseRate())
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
     * Returns list of CurrencyRate from file.
     */
    @Override
    public List<CurrencyRate> getExchangeRate(String date) {
        List<String> csvLines;

        try {
            csvLines = Files.readAllLines(createFullPath(date), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }

        List<CurrencyRate> currencyRateList = new ArrayList<>();

        for (String csvLine : csvLines) {
            String[] parts = csvLine.split(", ");
            currencyRateList.add(new CurrencyRate(
                             Currency.getInstance(parts[0]),
                             new BigDecimal(parts[1]),
                             new BigDecimal(parts[2])
            ));
        }

        return currencyRateList;
    }
}
