package swingarchivizer.workers;

import swingarchivizer.ErrorReporter;
import swingarchivizer.HttpService;
import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchItem;
import swingarchivizer.dto.Item;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public final class FetchItemsWorker extends SwingWorker<List<Item>, Void> {

    private final AppContext ctx;
    private final ErrorReporter errorReporter;
    private final HttpService httpService;
    private final LocalDate from;
    private final LocalDate to;

    public FetchItemsWorker(
            AppContext ctx,
            ErrorReporter errorReporter,
            HttpService httpService,
            LocalDate from,
            LocalDate to
    ) {
        super();

        this.ctx = ctx;
        this.errorReporter = errorReporter;
        this.httpService = httpService;
        this.from = from;
        this.to = to;
    }

    @Override
    protected List<Item> doInBackground() throws IOException {
        return Arrays.asList(httpService.postItemsIds(from, to));
    }

    @Override
    protected void done() {
        try {
            List<FetchItem> items = get().stream()
                    .map(FetchItem::new)
                    .collect(Collectors.toList());
            ctx.getItems().setValue(items);
        } catch (InterruptedException | ExecutionException e) {
            errorReporter.report(e);
        } finally {
            ctx.getUiBusy().setValue(false);
        }
    }
}
