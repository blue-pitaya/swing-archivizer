package swingarchivizer.download;

import swingarchivizer.FetchItem;
import swingarchivizer.FetchService;
import swingarchivizer.UiBlocker;
import swingarchivizer.ValidationEvent;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public final class DownloadHandlerImpl implements DownloadHandler {

    private final FetchService fetchService;
    private final UiBlocker uiBlocker;

    private DownloadListener listener;

    public DownloadHandlerImpl(
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
            listener.directoryValidated(ValidationEvent.message("Nieprawidłowy katalog"));
            return;
        }

        listener.directoryValidated(ValidationEvent.ok());
        listener.fetchStateChanged(DownloadPanel.FetchState.RUNNING);
        uiBlocker.setBusy(UiBlocker.Mode.DOWNLOAD, true);
        downloadNext(items.iterator(), downloadDir);
    }

    @Override
    public void setListener(DownloadListener listener) {
        this.listener = listener;
    }

    public void showItems(List<FetchItem> items) {
        listener.itemsChanged(items);
        listener.fetchStateChanged(DownloadPanel.FetchState.NEW);
    }

    private void downloadNext(Iterator<FetchItem> items, Path downloadDir) {
        if (!items.hasNext()) {
            listener.fetchStateChanged(DownloadPanel.FetchState.FINISHED);
            uiBlocker.setBusy(UiBlocker.Mode.DOWNLOAD, false);
            listener.finished();
            return;
        }

        FetchItem item = items.next();
        listener.itemStatusChanged(item, FetchItem.Status.DOWNLOADING);
        fetchService.downloadItem(item, downloadDir, status -> {
            listener.itemStatusChanged(item, status);
            downloadNext(items, downloadDir);
        }, listener::errorOccurred);
    }
}
