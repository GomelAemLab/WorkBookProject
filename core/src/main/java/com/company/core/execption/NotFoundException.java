package com.company.core.execption;

import static com.company.core.constants.Constants.NOT_FOUND;

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