package pl.parser.nbp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.parser.nbp.domain.CurrencyForm;
import pl.parser.nbp.domain.ExchangeRate;
import pl.parser.nbp.service.rest_client.ExchangeRateRestService;
import pl.parser.nbp.util.MathUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author krzykrucz.
 */

@Service
public class ExchangeRateCalculator {

    @Autowired
    private ExchangeRateRestService exchangeRateRestService;

    public List<Double> getResults(CurrencyForm form) {

        List<ExchangeRate> exchangeRateList =
                exchangeRateRestService.getExchangeRateTablesList(form.getStartDate(), form.getEndDate())
                        .stream()
                        .flatMap(table -> table.getExchangeRates().stream())
                        .filter(rate ->
                                rate.getCurrencyUnit().equals(form.getCurrencyUnit()))  //currency unit matches
                        .collect(Collectors.toList());

        return Arrays.asList(
                calculateAvgBuyingRate(exchangeRateList),
                calculateSellingRateStdDeviation(exchangeRateList)
        );
    }

    private Double calculateSellingRateStdDeviation(List<ExchangeRate> exchangeRateList) {

        double[] rates = exchangeRateList
                .stream()
                .mapToDouble(ExchangeRate::getSellingRate)
                .toArray();

        return MathUtils.stdDeviation(rates)
                .orElse(Double.NaN);
    }


    private Double calculateAvgBuyingRate(List<ExchangeRate> exchangeRateList) {

        double[] rates = exchangeRateList
                .stream()
                .mapToDouble(ExchangeRate::getBuyingRate)
                .toArray();

        return Arrays.stream(rates).average()
                .orElse(Double.NaN);
    }
}
