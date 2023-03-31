package controller;

import exceptions.*;
import model.CurrencyRate;
import model.LocalCurrency;
import service.CurrencyRateService;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CurrencyRateConsoleController implements CurrencyRateController {

    /**Maximum number of arguments in the putExchangeRate command */
    private final static int PUT_RATE_MAX_ARGUMENTS = 4;

    /** Maximum number of arguments in the removeExchangeRate command */
    private final static int REMOVE_RATE_MAX_ARGUMENTS = 2;

    /** Maximum number of arguments in the listExchangeRates command */
    private final static int GET_LIST_MAX_ARGUMENTS = 1;
    private final CurrencyRateService service;
    private final LocalCurrency localCurrency;

    public CurrencyRateConsoleController(CurrencyRateService service, LocalCurrency localCurrency) {
        Objects.requireNonNull(service);
        Objects.requireNonNull(localCurrency);

        this.service = service;
        this.localCurrency = localCurrency;
    }

    @Override
    public void run(String command, List<String> argumentsList) {
        try {
            switch (command) {
                case "admin/putExchangeRate" -> putRate(argumentsList);
                case "admin/removeExchangeRate" -> removeRate(argumentsList);
                case "listExchangeRates" -> getListExchangeRate(argumentsList);
                default -> throw new UnknownCommandException("Неизвестная команда");
            }
        } catch (ApplicationException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception exception) {
            System.out.println("Неизвестная ошибка");
        }
    }

    /**
     * Gets a list of arguments and creates a CurrencyRate model.
     * Then add it to a file with a specific date.
     */
    private void putRate(List<String> argumentsList) {
        if (argumentsList.size() != PUT_RATE_MAX_ARGUMENTS)
            throw new IncorrectCommandFormatException("Неверный формат команды");

        if (!isDateFormatValid(argumentsList.get(0)))
            throw new InvalidDateFormatException("Неверный формат даты");

        if (!isCurrencyValid(argumentsList.get(1)))
            throw new UnsupportedCurrencyException("Неподдерживаемая валюта");

        isPurchaseRateValid(argumentsList.get(2));
        isSellingRateValid(argumentsList.get(3));

        service.saveExchangeRate(argumentsList.get(0),
                                 new CurrencyRate(Currency.getInstance(argumentsList.get(1)),
                                 new BigDecimal(argumentsList.get(3)),
                                 new BigDecimal(argumentsList.get(2)))
        );

        System.out.println("Запись сохранена");
    }

    /**
     * Gets a list of arguments and remove currency from file if it exists.
     */
    private void removeRate(List<String> argumentsList) {
        if (argumentsList.size() != REMOVE_RATE_MAX_ARGUMENTS)
            throw new IncorrectCommandFormatException("Неверный формат команды");

        if (!isDateFormatValid(argumentsList.get(0)))
            throw new InvalidDateFormatException("Неверный формат даты");

        if (!isCurrencyValid(argumentsList.get(1)))
            throw new UnsupportedCurrencyException("Неподдерживаемая валюта");

        if (service.removeExchangeRate(argumentsList.get(0), argumentsList.get(1)))
            System.out.println("Успешное удаление");
        else System.out.println("Записи не существует");
    }

    /**
     * Gets a list of currency rates from a file and outputs it
     */
    private void getListExchangeRate(List<String> argumentsList) {
        if (argumentsList.size() != GET_LIST_MAX_ARGUMENTS)
            throw new IncorrectCommandFormatException("Неверный формат команды");

        if (!isDateFormatValid(argumentsList.get(0)))
            throw new InvalidDateFormatException("Неверный формат даты");

        List<CurrencyRate> exchangeRateList = service.getList(argumentsList.get(0));

        if (exchangeRateList.size() == 0)
            System.out.println("Данные отсутствуют");
        else printRates(exchangeRateList);
    }

    private boolean isDateFormatValid(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isCurrencyValid(String currency) {

        if (currency.equals(localCurrency.getLocalCurrency().getCurrencyCode()))
            throw new LocalCurrencyException("Местная валюта");

        Set<Currency> setOfCurrency = Currency.getAvailableCurrencies();
        for (Currency currencyElem : setOfCurrency) {
            if (currencyElem.getCurrencyCode().equals(currency))
                return true;
        }

        return false;
    }

    private void isPurchaseRateValid(String purchaseRate) {
        if (purchaseRate.trim().charAt(0) == '-')
            throw new ExchangeRateValueException("Неверное значение курса покупки");

        if (!purchaseRate.matches("\\d+(\\.\\d+)?"))
            throw new ExchangeRateFormatException("Неверный формат курса покупки");
    }

    private void isSellingRateValid(String sellingRate) {
        if (sellingRate.trim().charAt(0) == '-')
            throw new ExchangeRateValueException("Неверное значение курса продажи");

        if (!sellingRate.matches("\\d+(\\.\\d+)?"))
            throw new ExchangeRateFormatException("Неверный формат курса продажи");
    }

    /**
     * Outputs all exchange rates to console
     */
    private void printRates(List<CurrencyRate> currencyRateList) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-10s %s\n", "Валюта", "Покупка", "Продажа"));
        for (CurrencyRate currency : currencyRateList) {
            sb.append(String.format("%-10s %-10.3s %.3s\n", currency.getCurrency(), currency.getPurchaseRate(),
                             currency.getSellingRate()));
        }

        System.out.println(sb);
    }
}
