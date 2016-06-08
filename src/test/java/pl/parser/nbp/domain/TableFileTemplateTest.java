package pl.parser.nbp.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author krzykrucz.
 */
public class TableFileTemplateTest {

    private TableFileTemplate fileTemplate;
    private final static String SAMPLE_FILE_NAME = "c005z150109";
    private final static String SAMPLE_FILE_EXT = "xml";

    @Before
    public void setUp() throws Exception {
        fileTemplate = new TableFileTemplate(SAMPLE_FILE_NAME, SAMPLE_FILE_EXT);
    }

    @Test
    public void nameTest() throws Exception {
        assertThat(fileTemplate.getName(), is(equalTo(SAMPLE_FILE_NAME)));
    }

    @Test
    public void extTest() throws Exception {
        assertThat(fileTemplate.getExt(), is(equalTo(SAMPLE_FILE_EXT)));
    }

    @Test
    public void fullNameTest() throws Exception {
        assertThat(fileTemplate.getFullName(), is(equalTo(SAMPLE_FILE_NAME + "." + SAMPLE_FILE_EXT)));
    }

    @Test
    public void dateTest() throws Exception {
        LocalDate expectedDate = LocalDate.of(2015, 1, 9);
        assertThat(fileTemplate.getDate(), is(equalTo(expectedDate)));
    }

    @Test
    public void typeTest() throws Exception {
        char expectedChar = 'c';
        assertThat(fileTemplate.getType(), is(equalTo(expectedChar)));
    }

}