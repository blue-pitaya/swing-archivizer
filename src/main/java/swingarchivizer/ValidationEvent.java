package swingarchivizer;

final class ValidationEvent {

    private static final ValidationEvent OK = new ValidationEvent(null);

    private final String message;

    private ValidationEvent(String message) {
        this.message = message;
    }

    public static ValidationEvent ok() {
        return OK;
    }

    public static ValidationEvent message(String message) {
        return new ValidationEvent(message);
    }

    public boolean isOk() {
        return message == null;
    }

    public String getMessage() {
        return message;
    }
}
