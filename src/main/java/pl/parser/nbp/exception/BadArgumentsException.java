package pl.parser.nbp.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author krzykrucz.
 */
public class BadArgumentsException extends RuntimeException {

    private final static String MESSAGE = "Improper arguments: ";
    private List<ObjectError> globalErrors;

    public BadArgumentsException(BindingResult result) {
//        super();
        this.globalErrors = result.getGlobalErrors();
    }

    @Override
    public String getMessage() {

        StringBuilder message = new StringBuilder(MESSAGE).append("{");

        globalErrors.forEach(e ->
                message
                        .append(e.getCode())
                        .append(", "));

        message.delete(message.length() - 2, message.length()); //delete last ", "

        return message.append("}").toString();
    }
}
