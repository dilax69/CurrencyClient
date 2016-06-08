package pl.parser.nbp.domain;

import org.joda.money.CurrencyUnit;
import pl.parser.nbp.domain.adapter.CommaDoubleAdapter;
import pl.parser.nbp.domain.adapter.CurrencyUnitAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author krzykrucz.
 */

@XmlRootElement(name = "pozycja")
public class ExchangeRate {

    private double sellingRate;
    private double buyingRate;
    private CurrencyUnit currencyUnit;

    @XmlElement(name = "kod_waluty")
    @XmlJavaTypeAdapter(CurrencyUnitAdapter.class)
    public void setCurrencyUnit(CurrencyUnit currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    @XmlElement(name = "kurs_sprzedazy")
    @XmlJavaTypeAdapter(CommaDoubleAdapter.class)
    public void setSellingRate(Double sellingRate) {
        this.sellingRate = sellingRate;
    }

    @XmlElement(name = "kurs_kupna")
    @XmlJavaTypeAdapter(CommaDoubleAdapter.class)
    public void setBuyingRate(Double buyingRate) {
        this.buyingRate = buyingRate;
    }

    public Double getSellingRate() {
        return sellingRate;
    }

    public Double getBuyingRate() {
        return buyingRate;
    }

    public CurrencyUnit getCurrencyUnit() {
        return currencyUnit;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ExchangeRate) {
            ExchangeRate that = (ExchangeRate) obj;
            result = that.currencyUnit.equals(this.currencyUnit)
                    && Double.valueOf(that.sellingRate).equals(this.sellingRate)
                    && Double.valueOf(that.buyingRate).equals(this.buyingRate);
        }
        return result;
    }
}
