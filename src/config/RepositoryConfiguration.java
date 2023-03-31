package config;

import java.nio.file.Path;
import java.util.Objects;

public class RepositoryConfiguration {

    /**The path to currency data*/
    private final Path pathToDataStore;

    public RepositoryConfiguration(Path pathToDataStore) {
        Objects.requireNonNull(pathToDataStore);

        this.pathToDataStore = pathToDataStore;
    }

    public Path getPathToDataStore() {
        return pathToDataStore;
    }
}
