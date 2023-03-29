package repository;

import config.RepositoryConfiguration;
import model.CurrencyRate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class CurrencyRepository {
    private final RepositoryConfiguration config;

    public CurrencyRepository(RepositoryConfiguration config) {
        this.config = config;
    }

    public boolean putExchangeRate(List<CurrencyRate> currencyRateList) {
        try (BufferedWriter writer = Files.newBufferedWriter(config.getPathToDataStore(), StandardCharsets.UTF_8)) {
            writer.write(String.valueOf(currencyRateList.get(0)));
            writer.write(", ");
            writer.write(String.valueOf(currencyRateList.get(1)));
            writer.write(", ");
            writer.write(String.valueOf(currencyRateList.get(2)));
            writer.write(System.lineSeparator());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        return true;
    }
}
