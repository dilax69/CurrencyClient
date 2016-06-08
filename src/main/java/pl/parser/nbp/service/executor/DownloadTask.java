package pl.parser.nbp.service.executor;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

/**
 * @author krzykrucz.
 */
public class DownloadTask<T> implements Callable<T> {

    private final RestTemplate restTemplate;
    private final String url;
    private Class<T> tClass;

    public DownloadTask(RestTemplate restTemplate, String url, Class<T> tClass) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.tClass = tClass;
    }

    @Override
    public T call() throws Exception {
        return restTemplate.getForObject(url, tClass);
    }
}
