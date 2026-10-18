package swingarchivizer;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class DownloadPanel extends JPanel implements BusyAware {

    public enum FetchState {
        NEW("Gotowy"),
        RUNNING("Pobieranie w trakcie..."),
        STOPPED("Zatrzymano pobieranie"),
        FINISHED("Zakończono");

        private final String label;

        FetchState(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private final Notifier notifier;
    private final FetchService fetchService;
    private final BusyAware downloadBlocker;

    private final FetchItemsTable table = new FetchItemsTable();
    private final UniDirectoryInput directoryInput = new UniDirectoryInput("Katalog docelowy");
    private final JLabel statusLabel = new JLabel();
    private final JButton startButton = new JButton("Rozpocznij");
    private final JButton stopButton = new JButton("Zatrzymaj");

    private boolean busy;
    private FetchState fetchState;

    public DownloadPanel(
            Notifier notifier,
            FetchService fetchService,
            BusyAware downloadBlocker
    ) {
        super(new GridBagLayout());

        this.notifier = notifier;
        this.fetchService = fetchService;
        this.downloadBlocker = downloadBlocker;

        GridBagConstraints tableGridBagConstraints = getConstraints(0);
        tableGridBagConstraints.fill = GridBagConstraints.BOTH;
        tableGridBagConstraints.weightx = 1;
        tableGridBagConstraints.weighty = 1;
        add(new JScrollPane(table), tableGridBagConstraints);

        directoryInput.setPath(Paths.get(System.getProperty("user.dir"), "download"));
        add(directoryInput, getConstraints(1));

        add(statusLabel, getConstraints(2));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        add(buttonsPanel, getConstraints(3));

        startButton.addActionListener(event -> start());
        setFetchState(FetchState.NEW);
    }

    public void setItems(List<FetchItem> items) {
        table.setItems(items);
        updateButtons();
    }

    public void setFetchState(FetchState fetchState) {
        this.fetchState = fetchState;
        statusLabel.setText(fetchState.toString());
        updateDirectoryInput();
        updateButtons();
    }

    @Override
    public void setBusy(boolean busy) {
        this.busy = busy;
        table.setEnabled(!busy);
        updateDirectoryInput();
        updateButtons();
    }

    private void start() {
        Optional<Path> path = directoryInput.getValue();
        if (!path.isPresent()) {
            return;
        }

        setFetchState(FetchState.RUNNING);
        downloadBlocker.setBusy(true);
        Path downloadDir = path.get();
        List<FetchItem> items = table.getItems();
        downloadNext(items.iterator(), downloadDir);
    }

    private void downloadNext(Iterator<FetchItem> items, Path downloadDir) {
        if (!items.hasNext()) {
            setFetchState(FetchState.FINISHED);
            downloadBlocker.setBusy(false);
            notifier.showSuccess("Zakończono pobieranie");
            return;
        }

        FetchItem item = items.next();
        table.setStatus(item, FetchItem.Status.DOWNLOADING);
        fetchService.downloadItem(item, downloadDir, status -> {
            table.setStatus(item, status);
            downloadNext(items, downloadDir);
        });
    }

    private void updateDirectoryInput() {
        directoryInput.setEnabled(!busy && fetchState != FetchState.RUNNING);
    }

    private void updateButtons() {
        if (busy) {
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
            return;
        }

        boolean anyItems = !table.getItems().isEmpty();
        boolean finished = fetchState == FetchState.FINISHED;
        startButton.setEnabled(anyItems && !finished && fetchState != FetchState.RUNNING);
        stopButton.setEnabled(anyItems && !finished && fetchState != FetchState.NEW);
    }

    private static GridBagConstraints getConstraints(int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = y;
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.CENTER;

        return c;
    }
}
