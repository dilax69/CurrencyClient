package pl.parser.nbp.service.rest_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.parser.nbp.domain.ExchangeRatesTable;
import pl.parser.nbp.domain.TableFileTemplate;
import pl.parser.nbp.service.executor.DownloadTask;
import pl.parser.nbp.service.executor.ExecutorManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.parser.nbp.exception.ErrorMessage.RESTTEMPLATE_ERROR;

/**
 * @author krzykrucz.
 */

@Service
public class NBPExchangeRateRestService implements ExchangeRateRestService {

    private final static char TABLE_TYPE = 'c';
    private final String NBP_RATE_URL;
    private final RestTemplate restTemplate;
    private final ExecutorManager executorManager;

    @Autowired
    public NBPExchangeRateRestService(ExecutorManager executorManager,
                                      RestTemplate restTemplate,
                                      @Value("${url.nbp}") String url) {
        this.executorManager = executorManager;
        this.restTemplate = restTemplate;
        NBP_RATE_URL = url;
    }

    @Override
    public List<ExchangeRatesTable> getExchangeRateTablesList(LocalDate startDate, LocalDate endDate) {

        List<String> yearTxtFileNames = buildYearTxtFileNames(startDate, endDate);

        try {
            List<TableFileTemplate> tableFileTemplates = getXmlTableTemplates(yearTxtFileNames, startDate, endDate);
            return getExchangeRatesTables(tableFileTemplates);

        } catch (Exception e) {
            throw new RuntimeException(RESTTEMPLATE_ERROR.toString(), e);
        }
    }

    private List<ExchangeRatesTable> getExchangeRatesTables
            (List<TableFileTemplate> tableNames) throws Exception {
        List<Callable<ExchangeRatesTable>> tableDownloadTasks = tableNames.stream()
                .map(table -> new DownloadTask<>(restTemplate, buildURL(table.getFullName()), ExchangeRatesTable.class))
                .collect(Collectors.toList());
        return executorManager.execute(executorManager.getNewExecutor(), tableDownloadTasks);
    }

    private List<TableFileTemplate> getXmlTableTemplates
            (List<String> yearTxtFileNames, LocalDate startDate, LocalDate endDate) throws Exception {
        return yearTxtFileNames
                .stream()
                .map(name -> restTemplate.getForObject(buildURL(name), String.class)
                        .split("\\r?\\n"))   //every file name to new String
                .flatMap(Arrays::stream)
                .map(f -> new TableFileTemplate(f, "xml"))
                .filter(hasType(TABLE_TYPE))
                .filter(isWithinDates(startDate, endDate))
                .collect(Collectors.toList());
    }

    private Predicate<TableFileTemplate> isWithinDates(LocalDate startDate, LocalDate endDate) {
        return xmlFile ->
                xmlFile.getDate().compareTo(startDate) >= 0
                        && xmlFile.getDate().compareTo(endDate) <= 0;
    }

    private Predicate<TableFileTemplate> hasType(char type) {
        return f -> f.getType() == type;
    }

    private List<String> buildYearTxtFileNames(LocalDate startDate, LocalDate endDate) {
        List<String> yearTxtFileNames =
                IntStream
                        .rangeClosed(startDate.getYear(), endDate.getYear())
                        .filter(year -> year != LocalDate.now().getYear()) //because current year has different txt file name
                        .mapToObj(year -> buildYearFileName(Integer.toString(year)))
                        .collect(Collectors.toList());

        if (endDate.getYear() == LocalDate.now().getYear()) {   //current year case
            yearTxtFileNames.add(buildYearFileName(""));
        }
        return yearTxtFileNames;
    }

    private String buildYearFileName(String year) {
        return "dir" + year + ".txt";
    }

    private String buildURL(String path) {
        return NBP_RATE_URL + path;
    }

}
