package controller;

import exceptions.*;
import exceptions.ParseNumberFormatException;
import model.CurrencyRate;
import model.LocalCurrency;
import service.CurrencyRateService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CurrencyRateConsoleController implements CurrencyRateController {

    /** Maximum number of arguments in the putExchangeRate command */
    private final static int PUT_RATE_MAX_ARGUMENTS = 4;

    /** Maximum number of arguments in the removeExchangeRate command */
    private final static int REMOVE_RATE_MAX_ARGUMENTS = 2;

    /** Maximum number of arguments in the listExchangeRates command */
    private final static int GET_LIST_MAX_ARGUMENTS = 1;

    /** Maximum number of arguments in the exchange command */
    private final static int EXCHANGE_MAX_ARGUMENTS = 4;
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
                case "exchange" -> getExchangeRate(argumentsList);
                default -> throw new UnknownCommandException("Неизвестная команда");
            }
        } catch (ApplicationException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception exception) {
            System.out.println("Неизвестная ошибка");
        }
    }

    /**
     * Gets a list of arguments and creates a CurrencyRate model. Then add it's to a file with a specific date.
     * Params: argumentsList - list of arguments from a specific command
     * Throws: IncorrectCommandFormatException - if count of arguments don't match a specific command
     */
    private void putRate(List<String> argumentsList) {
        if (argumentsList.size() != PUT_RATE_MAX_ARGUMENTS)
            throw new IncorrectCommandFormatException("Неверный формат команды");

        if (isDateFormatValid(argumentsList.get(0)) && isLocalCurrency(argumentsList.get(1)) &&
                isCurrencyValid(argumentsList.get(1)) && isPurchaseRateValid(argumentsList.get(2)) &&
                isSellingRateValid(argumentsList.get(3))) {

            CurrencyRate currencyRate = new CurrencyRate(
                    Currency.getInstance(argumentsList.get(1)),
                    new BigDecimal(argumentsList.get(3)),
                    new BigDecimal(argumentsList.get(2))
            );

            Map<String, CurrencyRate> currencyMap = new HashMap<>();
            currencyMap.put(String.valueOf(Currency.getInstance(argumentsList.get(1))), currencyRate);

            service.saveExchangeRate(
                    LocalDate.parse(argumentsList.get(0)),
                    currencyMap
            );

            System.out.println("Запись сохранена");
        }
    }

    /**
     * Gets a list of arguments and remove currency from file if it's exists.
     * Params: argumentsList - list of arguments from a specific command
     * Throws: IncorrectCommandFormatException - if count of arguments don't match a specific command
     */
    private void removeRate(List<String> argumentsList) {
        if (argumentsList.size() != REMOVE_RATE_MAX_ARGUMENTS)
            throw new IncorrectCommandFormatException("Неверный формат команды");

        if (isDateFormatValid(argumentsList.get(0)) && isLocalCurrency(argumentsList.get(1)) &&
                isCurrencyValid(argumentsList.get(1))) {

            if (service.removeExchangeRate(LocalDate.parse(argumentsList.get(0)), argumentsList.get(1)))
                System.out.println("Успешное удаление");
            else System.out.println("Записи не существует");
        }
    }

    /**
     * Gets a list of currency rates from a file and outputs it.
     * Params: argumentsList - list of arguments from a specific command
     * Throws: IncorrectCommandFormatException - if count of arguments don't match a specific command
     */
    private void getListExchangeRate(List<String> argumentsList) {
        if (argumentsList.size() != GET_LIST_MAX_ARGUMENTS)
            throw new IncorrectCommandFormatException("Неверный формат команды");

        if (isDateFormatValid(argumentsList.get(0))) {
            Map<String, CurrencyRate> exchangeRateMap = service.getMap(LocalDate.parse(argumentsList.get(0)));

            if (exchangeRateMap.size() == 0)
                System.out.println("Данные отсутствуют");
            else printRates(exchangeRateMap);
        }
    }

    /**
     * Gets list of arguments, checks and performs the exchange
     * Params: argumentsList - list of arguments from a specific command
     * Throws: IncorrectCommandFormatException - if count of arguments don't match a specific command
     */
    private void getExchangeRate(List<String> argumentsList) {
        if (argumentsList.size() != EXCHANGE_MAX_ARGUMENTS)
            throw new IncorrectCommandFormatException("Неверный формат команды");

        if (isDateFormatValid(argumentsList.get(0)) && isSourceAmountFormatCorrect(argumentsList.get(1)) &&
                isInitialAmountValueCorrect(argumentsList.get(1)) &&
                isInitialCurrencyUnsupported(argumentsList.get(2), argumentsList.get(3))
        ) {
            BigDecimal amount = new BigDecimal(String.valueOf(argumentsList.get(1)));
            Currency fromCurrency = Currency.getInstance(argumentsList.get((2)));
            Currency toCurrency = Currency.getInstance(argumentsList.get((3)));

            BigDecimal result = service.exchange(LocalDate.parse(argumentsList.get(0)), amount, fromCurrency,
                    toCurrency, localCurrency);

            System.out.println(result);
        }
    }

    /**
     * The method checks date and return true, if the date is correct.
     * Params: date - The date of the exchange rate that the user entered
     * Throws: InvalidDateFormatException - if user entered an incorrect date format
     */
    private boolean isDateFormatValid(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception exception) {
            throw new InvalidDateFormatException("Неверный формат даты");
        }
    }

    /**
     * Checks that currency is supported
     * Params: currency - input currency in ISO format
     * Throws: UnsupportedCurrencyException - if currency is unsupported
     */
    private boolean isCurrencyValid(String currency) {
        try {
            Currency.getInstance(currency);
            return true;
        } catch (Exception exception) {
            throw new UnsupportedCurrencyException("Неподдерживаемая валюта");
        }
    }

    /**
     * Checks that input currency isn't local currency
     * Params: currency - input currency in ISO format
     * Throws: LocalCurrencyException - if input currency is local
     */
    private boolean isLocalCurrency(String currency) {
        if (currency.equals(localCurrency.getLocalCurrency().getCurrencyCode()))
            throw new LocalCurrencyException("Местная валюта");

        return true;
    }

    /**
     * Method checks that input purchase rate is valid
     * Params: purchaseRate - input purchase rate
     * Throws: ParseNumberFormatException - if format of purchase rate is invalid
     *         ExchangeRateValueException - if purchase rate is negative
     */
    private boolean isPurchaseRateValid(String purchaseRate) {
        try {
            Double.parseDouble(purchaseRate);
        } catch (Exception exception) {
            throw new ParseNumberFormatException("Неверный формат курса покупки");
        }

        if (BigDecimal.valueOf(Double.parseDouble(purchaseRate)).compareTo(BigDecimal.ZERO) < 0)
            throw new ExchangeRateValueException("Неверное значение курса покупки");

        return true;
    }

    /**
     * Method checks that input selling rate is valid
     * Params: sellingRate - input purchase rate
     * Throws: ParseNumberFormatException - if format of selling rate is invalid
     *         ExchangeRateValueException - if selling rate is negative
     */
    private boolean isSellingRateValid(String sellingRate) {
        try {
            Double.parseDouble(sellingRate);
        } catch (Exception exception) {
            throw new ParseNumberFormatException("Неверный формат курса продажи");
        }

        if (BigDecimal.valueOf(Double.parseDouble(sellingRate)).compareTo(BigDecimal.ZERO) < 0)
            throw new ExchangeRateValueException("Неверное значение курса продажи");

        return true;
    }

    /**
     * Outputs all exchange rates to console
     * Params: currencyRateList - list of exchange rates from file with specific date
     */
    private void printRates(Map<String, CurrencyRate> currencyRateMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-10s %s\n", "Валюта", "Покупка", "Продажа"));

        for (Map.Entry<String, CurrencyRate> entry : currencyRateMap.entrySet()) {
            sb.append(String.format("%-10s %-10.3s %.3s\n", entry.getValue().getCurrency(), entry.getValue().getPurchaseRate(),
                             entry.getValue().getSellingRate()));
        }

        System.out.println(sb);
    }

    /**
     * The method checks amount and return true if amount is correct
     * Params: amount - Initial amount required for the exchange
     * Throws: ParseNumberFormatException - incorrect amount format
     */
    private boolean isSourceAmountFormatCorrect(String amount) {
        try {
            Double.parseDouble(amount);
            return true;
        } catch (Exception exception) {
            throw new ParseNumberFormatException("Неверный формат исходной денежной суммы");
        }
    }

    /**
     * The method checks input amount and return true if amount is non-negative
     * Params: amount - input initial amount
     * Throws: InputAmountValueException - if amount is non-negative
     */
    private boolean isInitialAmountValueCorrect(String amount) {
        if (BigDecimal.valueOf(Double.parseDouble(amount)).compareTo(BigDecimal.ZERO) < 0)
            throw new InputAmountValueException("Неверное значение исходной денежной суммы");

        return true;
    }

    /**
     * The method checks that the initial currency and target currency are supported
     * Params: fromCurrency - initial currency
     *         toCurrency - target currency
     * Throws: UnsupportedCurrencyException - if initial currency or target currency are unsupported
     */
    private boolean isInitialCurrencyUnsupported(String fromCurrency, String toCurrency) {
        try {
            Currency.getInstance(fromCurrency);
        } catch (Exception exception) {
            throw new UnsupportedCurrencyException("Неподдерживаемая исходная валюта");
        }

        try {
            Currency.getInstance(toCurrency);
        } catch (Exception exception) {
            throw new UnsupportedCurrencyException("Неподдерживаемая целевая валюта");
        }

        return true;
    }
}
