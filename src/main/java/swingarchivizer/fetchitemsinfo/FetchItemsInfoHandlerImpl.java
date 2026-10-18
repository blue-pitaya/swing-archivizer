package swingarchivizer.fetchitemsinfo;

import swingarchivizer.common.FetchItem;
import swingarchivizer.common.FetchService;
import swingarchivizer.common.UiBlocker;
import swingarchivizer.common.ValidationEvent;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Consumer;

public final class FetchItemsInfoHandlerImpl implements FetchItemsInfoHandler {

    private final FetchService fetchService;
    private final UiBlocker uiBlocker;
    private final Consumer<List<FetchItem>> onItemsFetched;

    private FetchItemsInfoListener listener;

    public FetchItemsInfoHandlerImpl(
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
            listener.fromValidated(ValidationEvent.ok());
        } catch (DateTimeParseException e) {
            listener.fromValidated(ValidationEvent.message("Nieprawidłowa data"));
        }

        LocalDate toDate = null;
        try {
            toDate = LocalDate.parse(to);
            listener.toValidated(ValidationEvent.ok());
        } catch (DateTimeParseException e) {
            listener.toValidated(ValidationEvent.message("Nieprawidłowa data"));
        }

        if (fromDate == null || toDate == null) {
            return;
        }

        if (fromDate.isAfter(toDate)) {
            listener.toValidated(ValidationEvent.message("Nieprawidłowa data"));
            return;
        }

        uiBlocker.setBusy(UiBlocker.Mode.FETCH, true);
        fetchService.fetchItems(fromDate, toDate, onItemsFetched, listener::errorOccurred);
    }

    @Override
    public void setListener(FetchItemsInfoListener listener) {
        this.listener = listener;
    }
}
