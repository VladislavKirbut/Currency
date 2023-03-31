package model;

import java.util.Currency;
import java.util.Objects;

public class LocalCurrency {

    /**
     * The local currency is the base currency through which all currency transfers take place.
     * Local currency set in environment variables
     */
    private final Currency localCurrency;

    public LocalCurrency(Currency localCurrency) {
        Objects.requireNonNull(localCurrency);

        this.localCurrency = localCurrency;
    }

    public Currency getLocalCurrency() {
        return localCurrency;
    }
}
