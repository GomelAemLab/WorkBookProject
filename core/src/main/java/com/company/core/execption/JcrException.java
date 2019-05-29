package com.company.core.execption;

import static com.company.core.constants.Constants.LOCKED;


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
