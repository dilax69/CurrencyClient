package pl.parser.nbp.service.rest_client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import pl.parser.nbp.MainClass;
import pl.parser.nbp.domain.ExchangeRatesTable;
import pl.parser.nbp.service.executor.ExecutorManager;
import pl.parser.nbp.service.executor.ExecutorManagerImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author krzykrucz.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainClass.class)
public class NBPRestServiceTest {

    private final static List<String> TEST_FILE_NAMES =
            Arrays.asList(
                    "dir2014.txt",
                    "dir2015.txt",
                    "c094z140516.xml"
            );
    @Value("${url.nbp}")
    private String NBP_RATE_URL;
    private ExchangeRateRestService exchangeRateRestService;
    private MockRestServiceServer mockServer;
    private List<ExchangeRatesTable> expectedTables;

    @Before
    public void setUp() throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        ExecutorManager executorManagerMocked = mock(ExecutorManagerImpl.class);
        when(executorManagerMocked.execute(anyObject(), anyObject())).thenCallRealMethod();
        when(executorManagerMocked.getNewExecutor())
                .thenAnswer(invocationOnMock -> Executors.newSingleThreadExecutor());   //to keep order of calls to mock server

        exchangeRateRestService = new NBPExchangeRateRestService(executorManagerMocked, restTemplate, NBP_RATE_URL);

        mockServer = MockRestServiceServer.createServer(restTemplate);

        TEST_FILE_NAMES.forEach(file -> {
            try {
                mockServer.expect(requestTo(NBP_RATE_URL + file))
                        .andRespond(withSuccess(Files.readAllBytes(getPath(file)), getMediaType(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    public void getExchangeRateTablesTest() throws Exception {

        initExpectedTables("c094z140516.xml");
        final LocalDate startDate = LocalDate.of(2014, 5, 16);
        final LocalDate endDate = LocalDate.of(2015, 1, 8);

        List<ExchangeRatesTable> tables =
                exchangeRateRestService.getExchangeRateTablesList(startDate, endDate);

        mockServer.verify();
        assertThat(tables, is(equalTo(expectedTables)));

    }


    private MediaType getMediaType(String fileName) {
        return fileName.startsWith("dir") ? MediaType.TEXT_HTML : MediaType.TEXT_XML;

    }

    private Path getPath(String fileName) {

        URL file = NBPRestServiceTest.class.getResource("/" + fileName);
        assertNotNull("File not found", file);
        try {
            return Paths.get(file.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("File not accessible", e);
        }

    }

    private void initExpectedTables(String... fileNames) throws JAXBException {

        expectedTables = new LinkedList<>();
        for(String fileName : fileNames) {
            File xmlTable = new File("src/test/resources/" + fileName);
            JAXBContext jaxb = JAXBContext.newInstance(ExchangeRatesTable.class);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            expectedTables.add((ExchangeRatesTable) unmarshaller.unmarshal(xmlTable));
        }
    }

}
