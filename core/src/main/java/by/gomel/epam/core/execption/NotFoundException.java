package by.gomel.epam.core.execption;

import static by.gomel.epam.core.constants.Constants.NOT_FOUND;

public class NotFoundException extends HttpException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return NOT_FOUND;
    }
}