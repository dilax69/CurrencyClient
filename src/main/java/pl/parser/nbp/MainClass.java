package pl.parser.nbp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import pl.parser.nbp.domain.CurrencyForm;
import pl.parser.nbp.exception.BadArgumentsException;
import pl.parser.nbp.exception.GlobalExceptionHandler;
import pl.parser.nbp.service.ExchangeRateCalculator;

/**
 * @author krzykrucz.
 *
 *         To run this application: (specific info in README.md)
 *         mvn package
 *         java -jar target/currencyclient-1.0-RELEASE.jar args[]
 */

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class MainClass implements CommandLineRunner {

    @Autowired
    private ExchangeRateCalculator exchangeRateCalculator;
    @Autowired
    private Validator validator;

    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        SpringApplication.run(MainClass.class, args);
    }

    @Override
    public void run(String... strings) {

        validateArgs(strings);

        CurrencyForm args = new CurrencyForm(strings);

        exchangeRateCalculator.getResults(args)
                .forEach(d -> System.out.format("%.4f%n", d));

    }

    private void validateArgs(String[] args) {
        DataBinder binder = new DataBinder(args);
        binder.setValidator(validator);
        binder.validate();
        BindingResult result = binder.getBindingResult();
        if (result.hasErrors()) {
            throw new BadArgumentsException(result);
        }
    }

}
