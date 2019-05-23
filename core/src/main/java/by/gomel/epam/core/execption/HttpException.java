package by.gomel.epam.core.execption;

import static by.gomel.epam.core.constants.Constants.BAD_REQUEST;

public class HttpException extends Exception {

    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }

    public int getStatusCode(){
        return BAD_REQUEST;
    }
}
