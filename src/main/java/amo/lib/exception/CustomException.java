package amo.lib.exception;

public class CustomException extends Exception {
    private static final long serialVersionUID = 1L;

    private Integer eventType;
    private String message;

    public CustomException(Integer eventType, String message) {
        super(message);
        this.eventType = eventType;
        this.message = message;
    }
}
