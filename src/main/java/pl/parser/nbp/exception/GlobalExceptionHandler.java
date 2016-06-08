package pl.parser.nbp.exception;

/**
 * @author krzykrucz.
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //cause is necessary due to CommandLineRunner catching exceptions
        System.out.println
                ("Application ends with exception " +
                        e.getCause().getClass() + ": "
                        + e.getCause().getMessage());
    }
}
