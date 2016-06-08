package pl.parser.nbp.domain.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author krzykrucz.
 */
public class CommaDoubleAdapter extends XmlAdapter<String, Double> {

    private static final char COMMA = ',';
    private static final char POINT = '.';

    @Override
    public Double unmarshal(String v) throws Exception {
        return Double.valueOf(v.replace(COMMA, POINT));
    }

    @Override
    public String marshal(Double v) throws Exception {
        return v.toString().replace(POINT, COMMA);
    }
}
