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

    private final Notifier notifier;
    private final FetchService fetchService;

    private Consumer<ValidationEvent> directoryValidationListener;
    private Consumer<DownloadPanel.FetchState> fetchStateListener;
    private BiConsumer<FetchItem, FetchItem.Status> itemStatusListener;

    DownloadHandlerImpl(
            Notifier notifier,
            FetchService fetchService
    ) {
        this.notifier = notifier;
        this.fetchService = fetchService;
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

    private void downloadNext(Iterator<FetchItem> items, Path downloadDir) {
        if (!items.hasNext()) {
            fetchStateListener.accept(DownloadPanel.FetchState.FINISHED);
            notifier.showSuccess("Zakończono pobieranie");
            return;
        }

        FetchItem item = items.next();
        itemStatusListener.accept(item, FetchItem.Status.DOWNLOADING);
        fetchService.downloadItem(item, downloadDir, status -> {
            itemStatusListener.accept(item, status);
            downloadNext(items, downloadDir);
        });
    }
}
