package pl.parser.nbp.service;

import org.joda.money.CurrencyUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.parser.nbp.domain.CurrencyForm;
import pl.parser.nbp.domain.ExchangeRatesTable;
import pl.parser.nbp.service.rest_client.ExchangeRateRestService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author krzykrucz.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateCalculatorTest {

    private final static String ARG_CURRENCY = "USD";
    @Mock
    private CurrencyForm form;
    @Mock
    private ExchangeRateRestService exchangeRateRestService;
    @InjectMocks
    private ExchangeRateCalculator exchangeRateCalculator;

    @Before
    public void setUp() throws Exception {
        when(form.getCurrencyUnit()).thenReturn(CurrencyUnit.of(ARG_CURRENCY));
    }

    @Test
    public void shouldReturnGoodResult() throws Exception {
        //expected
        final double expectedAverage = 3.1502;
        final double expectedDeviation = 0.41088;

        //given
        List<ExchangeRatesTable> sampleTablesList = createSampleList();
        when(exchangeRateRestService.getExchangeRateTablesList(any(), any()))
                .thenReturn(sampleTablesList);
        //when
        List<Double> results = exchangeRateCalculator.getResults(form);

        //then
        final double error = 0.0001;
        assertThat(
                results,
                hasItems(
                        closeTo(expectedAverage, error),
                        closeTo(expectedDeviation, error)
                ));
    }

    @Test
    public void shouldReturnNaN() throws Exception {
        //given only 1 USD currency rate
        List<ExchangeRatesTable> sampleTablesList = createSampleList();
        sampleTablesList.remove(0);
        sampleTablesList.remove(0);
        when(exchangeRateRestService.getExchangeRateTablesList(any(), any()))
                .thenReturn(sampleTablesList);
        //when
        List<Double> results = exchangeRateCalculator.getResults(form);
        //then
        assertThat(results, hasItems(Double.NaN));

    }

    private List<ExchangeRatesTable> createSampleList() throws Exception {
        ExchangeRatesTable table1, table2, table3;

        File xmlTable1 = new File("src/test/resources/exchange-rate-table.xml");
        File xmlTable2 = new File("src/test/resources/c005z150109.xml");
        File xmlTable3 = new File("src/test/resources/c094z140516.xml");

        JAXBContext jaxb = JAXBContext.newInstance(ExchangeRatesTable.class);
        Unmarshaller unmarshaller = jaxb.createUnmarshaller();

        table1 = (ExchangeRatesTable) unmarshaller.unmarshal(xmlTable1);
        table2 = (ExchangeRatesTable) unmarshaller.unmarshal(xmlTable2);
        table3 = (ExchangeRatesTable) unmarshaller.unmarshal(xmlTable3);

        return new LinkedList<>(Arrays.asList(table1, table2, table3));
    }


}