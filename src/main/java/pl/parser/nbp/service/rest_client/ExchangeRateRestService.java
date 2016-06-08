package pl.parser.nbp.service.rest_client;

import pl.parser.nbp.domain.ExchangeRatesTable;

import java.time.LocalDate;
import java.util.List;

/**
 * @author krzykrucz.
 */
public interface ExchangeRateRestService {

    List<ExchangeRatesTable> getExchangeRateTablesList(LocalDate startDate, LocalDate endDate);

}
