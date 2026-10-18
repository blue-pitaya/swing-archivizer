package swingarchivizer.common;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public final class FetchService {

    private final HttpService httpService;
    private final UiBlocker uiBlocker;

    public FetchService(
            HttpService httpService,
            UiBlocker uiBlocker
    ) {
        this.httpService = httpService;
        this.uiBlocker = uiBlocker;
    }

    public void fetchItems(LocalDate from, LocalDate to, Consumer<List<FetchItem>> onFetched, Consumer<Exception> onError) {
        new SwingWorker<List<FetchItem>, Void>() {
            @Override
            protected List<FetchItem> doInBackground() throws IOException {
                return httpService.postItemsIds(from, to);
            }

            @Override
            protected void done() {
                try {
                    onFetched.accept(get());
                } catch (InterruptedException | ExecutionException e) {
                    onError.accept(e);
                } finally {
                    uiBlocker.setBusy(UiBlocker.Mode.FETCH, false);
                }
            }
        }.execute();
    }

    public void downloadItem(FetchItem item, Path downloadDir, Consumer<FetchItem.Status> onDone, Consumer<Exception> onError) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws IOException {
                httpService.fetchItem(item.id, downloadDir);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    onDone.accept(FetchItem.Status.DONE);
                } catch (InterruptedException | ExecutionException e) {
                    onError.accept(e);
                    onDone.accept(FetchItem.Status.ERROR);
                }
            }
        }.execute();
    }

}
