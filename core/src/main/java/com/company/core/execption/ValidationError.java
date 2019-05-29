package com.company.core.execption;

import static com.company.core.constants.Constants.UNPROCESSABLE_ENTITY;

public class ValidationError extends HttpException{
    public ValidationError() {
        super();
    }
    public ValidationError(String message) {
        super(message);
    }
    @Override
    public int getStatusCode(){
        return UNPROCESSABLE_ENTITY;
    }
}
