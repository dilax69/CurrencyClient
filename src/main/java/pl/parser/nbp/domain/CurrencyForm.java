package pl.parser.nbp.domain;

import org.joda.money.CurrencyUnit;

import java.time.LocalDate;

/**
 * @author krzykrucz.
 */
public class CurrencyForm {

    private CurrencyUnit currencyUnit;

    private LocalDate startDate;

    private LocalDate endDate;

    public CurrencyForm(String[] strings) {
        currencyUnit = CurrencyUnit.of(strings[0]);
        startDate = LocalDate.parse(strings[1]);
        endDate = LocalDate.parse(strings[2]);

        if (startDate.isAfter(endDate)) {
            swapDates();
        }
    }

    public CurrencyUnit getCurrencyUnit() {
        return currencyUnit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    private void swapDates() {
        LocalDate tmp = startDate;
        startDate = endDate;
        endDate = tmp;
    }
}
