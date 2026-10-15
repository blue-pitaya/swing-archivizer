package swingarchivizer.workers;

import swingarchivizer.HttpService;
import swingarchivizer.context.FetchItem;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;

public final class DownloadItemWorker extends SwingWorker<Void, Void> {

    private final FetchItem item;
    private final HttpService httpService;
    private final Path downloadDir;
    private final Runnable onDone;

    public DownloadItemWorker(
            FetchItem item,
            HttpService httpService,
            Path downloadDir,
            Runnable onDone
    ) {
        super();

        this.item = item;
        this.httpService = httpService;
        this.downloadDir = downloadDir;
        this.onDone = onDone;
    }

    @Override
    protected Void doInBackground() {
        try {
            httpService.fetchItem(item.value.id, downloadDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    protected void done() {
        onDone.run();
    }
}
