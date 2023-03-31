import config.RepositoryConfiguration;
import controller.ExchangeRateController;
import model.LocalCurrency;
import repository.CurrencyRepository;
import service.CurrencyService;

import java.nio.file.Path;
import java.util.Currency;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        Currency envCurrency = Currency.getInstance(System.getenv("LOCAL_CURRENCY_CODE"));
        LocalCurrency localCurrency = new LocalCurrency(envCurrency);

        Path pathToDataStore = Path.of("data", "exchange_rate");
        RepositoryConfiguration configuration = new RepositoryConfiguration(pathToDataStore);
        CurrencyRepository repository = new CurrencyRepository(configuration);
        CurrencyService service = new CurrencyService(repository);
        ExchangeRateController controller = new ExchangeRateController(service, localCurrency);

        if (args.length > 1) {
            String command = args[0];
            List<String> arguments = List.of(args).subList(1, args.length);
            controller.run(command, arguments);
        } else {
            controller.run("", List.of());
        }
    }
}
