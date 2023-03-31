package controller;

import exceptions.*;
import model.CurrencyRate;
import model.LocalCurrency;
import service.CurrencyService;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ExchangeRateController {
    private final static int PUT_EXCHANGE_MAX_ARGUMENTS = 4;
    private final CurrencyService service;

    private final LocalCurrency localCurrency;

    public ExchangeRateController(CurrencyService service, LocalCurrency localCurrency) {
        Objects.requireNonNull(service);
        Objects.requireNonNull(localCurrency);

        this.service = service;
        this.localCurrency = localCurrency;
    }

    public void run(String command, List<String> argumentsList) {
        try {
            switch (command) {
                case "admin/putExchangeRate" -> putExchangeRate(argumentsList);
                default -> throw new UnknownCommandException("Неизвестная команда");
            }
        } catch (ApplicationException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception exception) {
            System.out.println("Неизвестная ошибка");
        }
    }

    private void putExchangeRate(List<String> argumentsList) {
        if (argumentsList.size() != PUT_EXCHANGE_MAX_ARGUMENTS)
            throw new IncorrectCommandFormatException("Неверный формат команды");

        if (!isDateFormatValid(argumentsList.get(0)))
            throw new InvalidDateFormatException("Неверный формат даты");

        if (!isCurrencyValid(argumentsList.get(1)))
            throw new UnsupportedCurrencyException("Неподдерживаемая валюта");

        isPurchaseRateValid(argumentsList.get(2));
        isSellingRateValid(argumentsList.get(3));

        service.saveExchangeRate(argumentsList.get(0), new CurrencyRate(new BigDecimal(argumentsList.get(3)),
                Currency.getInstance(argumentsList.get(1)), new BigDecimal(argumentsList.get(2))));

        System.out.println("Запись сохранена");
    }

    private boolean isDateFormatValid(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isCurrencyValid(String currency) {
        if (Currency.getInstance(currency).equals(localCurrency.getLocalCurrency()))
            throw new LocalCurrencyException("Местная валюта");

        Set<Currency> setOfCurrency = Currency.getAvailableCurrencies();
        for (Currency currencyElem : setOfCurrency) {
            if (currencyElem.equals(Currency.getInstance(currency)))
                return true;
        }

        return false;
    }

    private void isPurchaseRateValid(String purchaseRate) {
        if (purchaseRate.trim().charAt(0) == '-')
            throw new ExchangeRateValueException("Неверное значение курса покупки");

        if (!purchaseRate.matches("\\d+(\\.)?\\d+"))
            throw new ExchangeRateFormatException("Неверный формат курса покупки");
    }
    private void isSellingRateValid(String sellingRate) {
        if (sellingRate.trim().charAt(0) == '-')
            throw new ExchangeRateValueException("Неверное значение курса продажи");

        if (!sellingRate.matches("\\d+(\\.)?\\d+"))
            throw new ExchangeRateFormatException("Неверный формат курса продажи");
    }
}
