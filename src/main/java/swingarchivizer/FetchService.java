package swingarchivizer;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class FetchService {

    private final Notifier notifier;
    private final HttpService httpService;
    private final BusyAware busyAware;

    public FetchService(
            Notifier notifier,
            HttpService httpService,
            BusyAware busyAware
    ) {
        this.notifier = notifier;
        this.httpService = httpService;
        this.busyAware = busyAware;
    }

    public void fetchItems(LocalDate from, LocalDate to, Consumer<List<FetchItem>> onFetched) {
        new SwingWorker<List<HttpService.Item>, Void>() {
            @Override
            protected List<HttpService.Item> doInBackground() throws IOException {
                return Arrays.asList(httpService.postItemsIds(from, to));
            }

            @Override
            protected void done() {
                try {
                    List<FetchItem> items = get().stream()
                            .map(FetchItem::new)
                            .collect(Collectors.toList());
                    onFetched.accept(items);
                } catch (InterruptedException | ExecutionException e) {
                    notifier.report(e);
                } finally {
                    busyAware.setBusy(false);
                }
            }
        }.execute();
    }

    public void downloadItem(FetchItem item, Path downloadDir, Consumer<FetchItem.Status> onDone) {
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
                    onDone.accept(FetchItem.Status.DONE);
                } catch (InterruptedException | ExecutionException e) {
                    notifier.report(e);
                    onDone.accept(FetchItem.Status.ERROR);
                }
            }
        }.execute();
    }

}
