package by.gomel.epam.core.execption;

import static by.gomel.epam.core.constants.Constants.LOCKED;


public class JcrException extends HttpException {

    public JcrException() {
        super();
    }

    public JcrException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return LOCKED;
    }
}
