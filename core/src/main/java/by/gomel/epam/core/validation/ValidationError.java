package by.gomel.epam.core.validation;

public class ValidationError extends Exception{
    public ValidationError(String message) {
        super(message);
    }
    public int getStatus(){
        return 422;
    }
}
