package repository;

import config.RepositoryConfiguration;
import model.CurrencyRate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class CurrencyRepository {
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
    public void putExchangeRate(String date, CurrencyRate currency) {
        try (BufferedWriter writer = Files.newBufferedWriter(createFullPath(date),
                StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(String.valueOf(currency.getCurrency()));
            writer.write(", ");
            writer.write(String.valueOf(currency.getSellingRate()));
            writer.write(", ");
            writer.write(String.valueOf(currency.getPurchaseRate()));
            writer.write(System.lineSeparator());
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
