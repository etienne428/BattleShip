package Utils;

public class BoatNotSetException extends Exception {
    private String message;

    public BoatNotSetException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
