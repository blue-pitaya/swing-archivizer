package swingarchivizer.fetchitemsinfo;

import swingarchivizer.FetchService;
import swingarchivizer.ItemsFetchedListener;
import swingarchivizer.UiBlocker;
import swingarchivizer.ValidationEvent;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class FetchItemsInfoHandlerImpl implements FetchItemsInfoHandler {

    private final FetchService fetchService;
    private final UiBlocker uiBlocker;
    private final ItemsFetchedListener onItemsFetched;

    private FetchItemsInfoListener listener;

    public FetchItemsInfoHandlerImpl(
            FetchService fetchService,
            UiBlocker uiBlocker,
            ItemsFetchedListener onItemsFetched
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
        fetchService.fetchItems(fromDate, toDate, onItemsFetched::itemsFetched, listener::errorOccurred);
    }

    @Override
    public void setListener(FetchItemsInfoListener listener) {
        this.listener = listener;
    }
}
