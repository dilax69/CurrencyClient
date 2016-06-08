package pl.parser.nbp.domain;

import org.joda.money.CurrencyUnit;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author krzykrucz.
 */
public class ExchangeRatesXmlTest {

    private ExchangeRatesTable table;

    @Before
    public void setUp() throws Exception {

        File xmlFile = new File("src/test/resources/exchange-rate-table.xml");
        JAXBContext jaxb = JAXBContext.newInstance(ExchangeRatesTable.class);
        Unmarshaller unmarshaller = jaxb.createUnmarshaller();
        table = (ExchangeRatesTable) unmarshaller.unmarshal(xmlFile);
    }

    @Test
    public void shouldUnmarshall() throws Exception {

        assertThat(table.getPublicationDate(), is(equalTo(LocalDate.of(2007, 4, 13))));
        assertThat(table.getExchangeRates(), hasSize(2));
        assertThat(table.getExchangeRates(), contains(
                hasProperty("sellingRate", is(2.878)),
                hasProperty("sellingRate", is(2.3762))
        ));
        assertThat(table.getExchangeRates(), contains(
                hasProperty("currencyUnit", is(CurrencyUnit.USD)),
                hasProperty("currencyUnit", is(CurrencyUnit.AUD))
        ));
    }

}