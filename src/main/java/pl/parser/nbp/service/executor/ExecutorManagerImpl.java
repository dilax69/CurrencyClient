package pl.parser.nbp.service.executor;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author krzykrucz.
 */

@Service
public class ExecutorManagerImpl implements ExecutorManager {

    private final static int NUMBER_OF_THREADS = 10;

    @Override
    public <T> List<T> execute(ExecutorService executorService, List<Callable<T>> tasks) throws Exception {

        List<Future<T>> futures;
        List<T> results = new LinkedList<>();
        futures = executorService.invokeAll(tasks);
        for (Future<T> future : futures) {
            results.add(future.get());
        }
        executorService.shutdown();

        return results;
    }

    @Override
    public ExecutorService getNewExecutor(){
        return Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }
}
