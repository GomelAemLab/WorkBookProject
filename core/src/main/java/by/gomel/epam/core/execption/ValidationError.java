package by.gomel.epam.core.execption;

import static by.gomel.epam.core.constants.Constants.UNPROCESSABLE_ENTITY;

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
