package swingarchivizer;

import java.util.function.Consumer;

public interface FetchItemsInfoHandler {

    void submitForm(String from, String to);

    void setFromValidationListener(Consumer<ValidationEvent> listener);

    void setToValidationListener(Consumer<ValidationEvent> listener);

    void setErrorListener(Consumer<Exception> listener);
}
