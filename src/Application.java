import config.RepositoryConfiguration;
import repository.CurrencyRepository;

import java.nio.file.Path;
public class Application {
    public static void main(String[] args) {
        Path pathToDataStore = Path.of("data", "exchange_rate");
        RepositoryConfiguration configuration = new RepositoryConfiguration(pathToDataStore);
        CurrencyRepository repository = new CurrencyRepository(configuration);
    }
}
