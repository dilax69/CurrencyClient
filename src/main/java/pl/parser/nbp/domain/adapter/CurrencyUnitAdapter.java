package pl.parser.nbp.domain.adapter;

import org.joda.money.CurrencyUnit;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.LinkedList;

/**
 * @author krzykrucz.
 */
public class CurrencyUnitAdapter extends XmlAdapter<String, CurrencyUnit> {

    @Override
    public CurrencyUnit unmarshal(String v) throws Exception {
        try {
            return CurrencyUnit.of(v);
        } catch (Exception e) {
            //register pseudo-currency if not existing
            return CurrencyUnit.registerCurrency(v, -1, -1, new LinkedList<>());
        }
    }

    @Override
    public String marshal(CurrencyUnit v) throws Exception {
        return v.toString();
    }
}
