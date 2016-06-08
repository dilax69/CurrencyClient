package pl.parser.nbp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Arrays;

import static pl.parser.nbp.exception.ErrorMessage.ARGUMENTS_NUMBER;
import static pl.parser.nbp.exception.ErrorMessage.INVALID_FORMAT;

/**
 * @author krzykrucz.
 */
@Component
public class ArgsValidator implements Validator {


    private final static String ARG_0 = "currencyUnit";
    private final static String ARG_1 = "startDate";
    private final static String ARG_2 = "endDate";

    @Override
    public boolean supports(Class<?> aClass) {
        return String[].class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        String[] args = (String[]) o;
        if (args.length != 3) {
            errors.reject(ARGUMENTS_NUMBER.toString());
            return;
        }
        if (!isValidCurrencyUnit(args[0])) {
            errors.reject(INVALID_FORMAT.toString() + ARG_0);
        }
        if (!isValidLocalDate(args[1])) {
            errors.reject(INVALID_FORMAT.toString() + ARG_1);
        }
        if (!isValidLocalDate(args[2])) {
            errors.reject(INVALID_FORMAT.toString() + ARG_2);
        }


    }

    private boolean isValidLocalDate(String dateString) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateString); //has proper format
        } catch (Exception e) {
            return false;
        }
        return date.isBefore(LocalDate.now());  //is in past
    }

    private boolean isValidCurrencyUnit(String currencyUnit) {
        return Arrays
                .stream(PermittedCurrencyUnits.values())
                .map(Enum::toString)
                .filter(currencyUnit::equals)
                .findAny()
                .isPresent();
    }
}
