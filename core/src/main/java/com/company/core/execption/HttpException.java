package com.company.core.execption;

import static com.company.core.constants.Constants.BAD_REQUEST;

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
