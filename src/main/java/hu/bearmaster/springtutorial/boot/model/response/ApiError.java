package hu.bearmaster.springtutorial.boot.model.response;

public class ApiError {
    private final String message;

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
