package swingarchivizer.presenters;

import swingarchivizer.HttpService;
import swingarchivizer.MessageDialogHelper;
import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchItem;
import swingarchivizer.context.FetchState;
import swingarchivizer.workers.DownloadItemWorker;

import javax.swing.*;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class DownloadPresenter {

    public interface View {
        void addStartListener(Runnable listener);

        Optional<Path> getDownloadDir();

        void setItems(List<FetchItem> items);

        void setTableEnabled(boolean enabled);

        void setDirectoryEnabled(boolean enabled);

        void setStartEnabled(boolean enabled);

        void setStopEnabled(boolean enabled);

        void setStatusText(String text);
    }

    private final View view;
    private final AppContext ctx;
    private final MessageDialogHelper messageDialogHelper;
    private final HttpService httpService;

    public DownloadPresenter(
            View view,
            AppContext ctx,
            MessageDialogHelper messageDialogHelper,
            HttpService httpService
    ) {
        this.view = view;
        this.ctx = ctx;
        this.messageDialogHelper = messageDialogHelper;
        this.httpService = httpService;

        view.addStartListener(this::start);
        ctx.getItems().subscribeAndCall(items -> SwingUtilities.invokeLater(() -> {
            view.setItems(items);
            updateButtons();
        }));
        ctx.getUiBusy().subscribeAndCall(uiBusy -> {
            view.setTableEnabled(!uiBusy);
            updateDirectoryInput();
            updateButtons();
        });
        ctx.getFetchState().subscribeAndCall(fetchState -> {
            view.setStatusText(fetchState.toString());
            updateDirectoryInput();
            updateButtons();
        });
    }

    private void start() {
        Optional<Path> path = view.getDownloadDir();
        if (!path.isPresent()) {
            return;
        }

        ctx.getFetchState().setValue(FetchState.RUNNING);
        Path downloadDir = path.get();
        List<FetchItem> items = ctx.getItems().getValue();
        downloadNext(items.iterator(), downloadDir);
    }

    private void downloadNext(Iterator<FetchItem> items, Path downloadDir) {
        if (!items.hasNext()) {
            ctx.getFetchState().setValue(FetchState.FINISHED);
            messageDialogHelper.success("Zakończono pobieranie");
            return;
        }

        FetchItem item = items.next();
        item.status.setValue(FetchItem.Status.DOWNLOADING);
        DownloadItemWorker worker = new DownloadItemWorker(
                item,
                messageDialogHelper,
                httpService,
                downloadDir,
                () -> downloadNext(items, downloadDir)
        );
        worker.execute();
    }

    private void updateDirectoryInput() {
        view.setDirectoryEnabled(!ctx.getUiBusy().getValue() && ctx.getFetchState().getValue() != FetchState.RUNNING);
    }

    private void updateButtons() {
        if (ctx.getUiBusy().getValue()) {
            view.setStartEnabled(false);
            view.setStopEnabled(false);
            return;
        }

        boolean anyItems = !ctx.getItems().getValue().isEmpty();
        boolean finished = ctx.getFetchState().getValue() == FetchState.FINISHED;
        view.setStartEnabled(anyItems && !finished && ctx.getFetchState().getValue() != FetchState.RUNNING);
        view.setStopEnabled(anyItems && !finished && ctx.getFetchState().getValue() != FetchState.NEW);
    }
}
