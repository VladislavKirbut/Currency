package config;

import java.nio.file.Path;

public class RepositoryConfiguration {

    /**The path to currency data*/
    private final Path pathToDataStore;

    public RepositoryConfiguration(Path pathToDataStore) {
        this.pathToDataStore = pathToDataStore;
    }

    public Path getPathToDataStore() {
        return pathToDataStore;
    }
}
