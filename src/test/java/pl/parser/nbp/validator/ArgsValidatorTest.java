package pl.parser.nbp.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static pl.parser.nbp.exception.ErrorMessage.INVALID_FORMAT;

/**
 * @author krzykrucz.
 */

@RunWith(Parameterized.class)
public class ArgsValidatorTest {

    private final static String OK_CURRENCY = "USD";
    private final static String OK_START_DATE = "2007-04-13";
    private final static String OK_END_DATE = "2008-11-24";
    private final static String BAD_CURRENCY = "AUD";
    private final static String MALFORMED_DATE = "20080617";
    private final static String FUTURE_DATE = LocalDate.now().plusDays(1).toString();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{BAD_CURRENCY, OK_START_DATE, OK_END_DATE}},
                {new String[]{OK_CURRENCY, MALFORMED_DATE, OK_END_DATE}},
                {new String[]{OK_CURRENCY, OK_START_DATE, FUTURE_DATE}}
        });
    }

    @Parameterized.Parameter
    public String[] args;

    private Validator argsValidator;

    @Before
    public void setUp() throws Exception {
        argsValidator = new ArgsValidator();
    }

    @Test
    public void shouldValidate() throws Exception {

        String[] args = new String[]{OK_CURRENCY, OK_START_DATE, OK_END_DATE};
        Errors errors = bindErrorsAndValidate(args);

        assertFalse(errors.hasErrors());

    }

    @Test
    public void shouldNotValidate() throws Exception {
        Errors errors = bindErrorsAndValidate(args);

        assertThat(errors.getGlobalErrorCount(), is(1));
        assertThat(errors.getGlobalError().getCode(), containsString(INVALID_FORMAT.toString()));
    }

    private Errors bindErrorsAndValidate(String[] args) {
        Errors errors = new BeanPropertyBindingResult(args, "args");
        argsValidator.validate(args, errors);
        return errors;
    }
}