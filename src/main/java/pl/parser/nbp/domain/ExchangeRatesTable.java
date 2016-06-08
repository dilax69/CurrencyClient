package pl.parser.nbp.domain;

import pl.parser.nbp.domain.adapter.LocalDateAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

/**
 * @author krzykrucz.
 */
@XmlRootElement(name = "tabela_kursow")
public class ExchangeRatesTable {

    private LocalDate publicationDate;

    private List<ExchangeRate> exchangeRates;

    @XmlElement(name = "data_publikacji")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    @XmlElement(name = "pozycja")
    public void setExchangeRates(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ExchangeRatesTable) {
            ExchangeRatesTable that = (ExchangeRatesTable) obj;
            result = that.exchangeRates.equals(this.exchangeRates) && that.publicationDate.equals(this.publicationDate);
        }
        return result;
    }
}
