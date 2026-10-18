package swingarchivizer;

import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchItem;
import swingarchivizer.context.FetchState;
import swingarchivizer.dto.Item;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public final class FetchService {

    private final AppContext ctx;
    private final MessageDialogHelper messageDialogHelper;
    private final HttpService httpService;
    private final BusyAware busyAware;

    public FetchService(
            AppContext ctx,
            MessageDialogHelper messageDialogHelper,
            HttpService httpService,
            BusyAware busyAware
    ) {
        this.ctx = ctx;
        this.messageDialogHelper = messageDialogHelper;
        this.httpService = httpService;
        this.busyAware = busyAware;
    }

    public void fetchItems(LocalDate from, LocalDate to) {
        new SwingWorker<List<Item>, Void>() {
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
                    ctx.getFetchState().setValue(FetchState.NEW);
                } catch (InterruptedException | ExecutionException e) {
                    messageDialogHelper.report(e);
                } finally {
                    busyAware.setBusy(false);
                }
            }
        }.execute();
    }

    public void downloadItem(FetchItem item, Path downloadDir, Runnable onDone) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws IOException {
                httpService.fetchItem(item.value.id, downloadDir);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    item.status.setValue(FetchItem.Status.DONE);
                } catch (InterruptedException | ExecutionException e) {
                    messageDialogHelper.report(e);
                    item.status.setValue(FetchItem.Status.ERROR);
                } finally {
                    onDone.run();
                }
            }
        }.execute();
    }

}
