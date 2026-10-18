package swingarchivizer;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

final class DownloadHandlerImpl implements DownloadHandler {

    private final FetchService fetchService;
    private final UiBlocker uiBlocker;

    private Consumer<ValidationEvent> directoryValidationListener;
    private Consumer<DownloadPanel.FetchState> fetchStateListener;
    private BiConsumer<FetchItem, FetchItem.Status> itemStatusListener;
    private Consumer<List<FetchItem>> itemsListener;
    private Consumer<Exception> errorListener;
    private Runnable finishedListener;

    DownloadHandlerImpl(
            FetchService fetchService,
            UiBlocker uiBlocker
    ) {
        this.fetchService = fetchService;
        this.uiBlocker = uiBlocker;
    }

    @Override
    public void startDownload(String directory, List<FetchItem> items) {
        Path downloadDir;
        try {
            downloadDir = Paths.get(directory);
        } catch (InvalidPathException e) {
            downloadDir = null;
        }

        if (directory.isEmpty() || downloadDir == null || !Files.isDirectory(downloadDir)) {
            directoryValidationListener.accept(ValidationEvent.message("Nieprawidłowy katalog"));
            return;
        }

        directoryValidationListener.accept(ValidationEvent.ok());
        fetchStateListener.accept(DownloadPanel.FetchState.RUNNING);
        uiBlocker.setBusy(UiBlocker.Mode.DOWNLOAD, true);
        downloadNext(items.iterator(), downloadDir);
    }

    @Override
    public void setDirectoryValidationListener(Consumer<ValidationEvent> listener) {
        directoryValidationListener = listener;
    }

    @Override
    public void setFetchStateListener(Consumer<DownloadPanel.FetchState> listener) {
        fetchStateListener = listener;
    }

    @Override
    public void setItemStatusListener(BiConsumer<FetchItem, FetchItem.Status> listener) {
        itemStatusListener = listener;
    }

    @Override
    public void setItemsListener(Consumer<List<FetchItem>> listener) {
        itemsListener = listener;
    }

    @Override
    public void setErrorListener(Consumer<Exception> listener) {
        errorListener = listener;
    }

    @Override
    public void setFinishedListener(Runnable listener) {
        finishedListener = listener;
    }

    void showItems(List<FetchItem> items) {
        itemsListener.accept(items);
        fetchStateListener.accept(DownloadPanel.FetchState.NEW);
    }

    private void downloadNext(Iterator<FetchItem> items, Path downloadDir) {
        if (!items.hasNext()) {
            fetchStateListener.accept(DownloadPanel.FetchState.FINISHED);
            uiBlocker.setBusy(UiBlocker.Mode.DOWNLOAD, false);
            finishedListener.run();
            return;
        }

        FetchItem item = items.next();
        itemStatusListener.accept(item, FetchItem.Status.DOWNLOADING);
        fetchService.downloadItem(item, downloadDir, status -> {
            itemStatusListener.accept(item, status);
            downloadNext(items, downloadDir);
        }, errorListener);
    }
}
