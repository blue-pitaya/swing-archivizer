package swingarchivizer;

public interface Notifier {

    void report(Exception e);

    void showSuccess(String message);
}
