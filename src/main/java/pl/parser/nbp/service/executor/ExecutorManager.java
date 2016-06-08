package pl.parser.nbp.service.executor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * @author krzykrucz.
 */
public interface ExecutorManager {
    <T> List<T> execute(ExecutorService executorService, List<Callable<T>> tasks) throws Exception;

    ExecutorService getNewExecutor();
}
