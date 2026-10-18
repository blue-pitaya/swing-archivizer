package swingarchivizer;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Consumer;

final class FetchItemsInfoHandlerImpl implements FetchItemsInfoHandler {

    private final FetchService fetchService;
    private final UiBlocker uiBlocker;
    private final Consumer<List<FetchItem>> onItemsFetched;

    private Consumer<ValidationEvent> fromValidationListener;
    private Consumer<ValidationEvent> toValidationListener;

    FetchItemsInfoHandlerImpl(
            FetchService fetchService,
            UiBlocker uiBlocker,
            Consumer<List<FetchItem>> onItemsFetched
    ) {
        this.fetchService = fetchService;
        this.uiBlocker = uiBlocker;
        this.onItemsFetched = onItemsFetched;
    }

    @Override
    public void submitForm(String from, String to) {
        LocalDate fromDate = null;
        try {
            fromDate = LocalDate.parse(from);
            fromValidationListener.accept(ValidationEvent.ok());
        } catch (DateTimeParseException e) {
            fromValidationListener.accept(ValidationEvent.message("Nieprawidłowa data"));
        }

        LocalDate toDate = null;
        try {
            toDate = LocalDate.parse(to);
            toValidationListener.accept(ValidationEvent.ok());
        } catch (DateTimeParseException e) {
            toValidationListener.accept(ValidationEvent.message("Nieprawidłowa data"));
        }

        if (fromDate == null || toDate == null) {
            return;
        }

        if (fromDate.isAfter(toDate)) {
            toValidationListener.accept(ValidationEvent.message("Nieprawidłowa data"));
            return;
        }

        uiBlocker.setBusy(true);
        fetchService.fetchItems(fromDate, toDate, onItemsFetched);
    }

    @Override
    public void setFromValidationListener(Consumer<ValidationEvent> listener) {
        fromValidationListener = listener;
    }

    @Override
    public void setToValidationListener(Consumer<ValidationEvent> listener) {
        toValidationListener = listener;
    }
}
