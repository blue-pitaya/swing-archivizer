package swingarchivizer.workers;

import swingarchivizer.ErrorReporter;
import swingarchivizer.HttpService;
import swingarchivizer.context.FetchItem;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public final class DownloadItemWorker extends SwingWorker<Void, Void> {

    private final FetchItem item;
    private final ErrorReporter errorReporter;
    private final HttpService httpService;
    private final Path downloadDir;
    private final Runnable onDone;

    public DownloadItemWorker(
            FetchItem item,
            ErrorReporter errorReporter,
            HttpService httpService,
            Path downloadDir,
            Runnable onDone
    ) {
        super();

        this.item = item;
        this.errorReporter = errorReporter;
        this.httpService = httpService;
        this.downloadDir = downloadDir;
        this.onDone = onDone;
    }

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
            errorReporter.report(e);
            item.status.setValue(FetchItem.Status.ERROR);
        } finally {
            onDone.run();
        }
    }
}
