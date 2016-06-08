package pl.parser.nbp.exception;

/**
 * @author krzykrucz.
 */
public enum ErrorMessage {
    ARGUMENTS_NUMBER("There should be 3 arguments."),
    INVALID_FORMAT("Invalid format of arg: "),
    RESTTEMPLATE_ERROR("Fetching currency data failed");

    private final String text;

    ErrorMessage(final String s) {
        text = s;
    }

    @Override
    public String toString() {
        return text;
    }
}
