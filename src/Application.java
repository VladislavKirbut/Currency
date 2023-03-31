import config.RepositoryConfiguration;
import controller.CurrencyRateConsoleController;
import model.LocalCurrency;
import repository.CurrencyRepository;
import service.CurrencyRateService;
import java.nio.file.Path;
import java.util.Currency;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        Currency envCurrency = Currency.getInstance(System.getenv("LOCAL_CURRENCY_CODE"));
        LocalCurrency localCurrency = new LocalCurrency(envCurrency);

        Path pathToDataStore = Path.of(System.getenv("DATA_STORE_PATH"));
        RepositoryConfiguration configuration = new RepositoryConfiguration(pathToDataStore);
        CurrencyRepository repository = new CurrencyRepository(configuration);
        CurrencyRateService service = new CurrencyRateService(repository);
        CurrencyRateConsoleController controller = new CurrencyRateConsoleController(service, localCurrency);

        if (args.length > 1) {
            String command = args[0];
            List<String> arguments = List.of(args).subList(1, args.length);
            controller.run(command, arguments);
        } else {
            controller.run("", List.of());
        }
    }
}
