package swingarchivizer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

final class DownloadPanel extends JPanel implements BusyAware {

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

    private final FetchItemsTable table = new FetchItemsTable();
    private final UniDirectoryInput directoryInput = new UniDirectoryInput("Katalog docelowy");
    private final JLabel statusLabel = new JLabel();
    private final JButton startButton = new JButton("Rozpocznij");
    private final JButton stopButton = new JButton("Zatrzymaj");

    private boolean busy;
    private FetchState fetchState;

    public DownloadPanel(
            AppConfig config,
            DownloadHandler handler
    ) {
        super(new GridBagLayout());

        GridBagConstraints tableGridBagConstraints = getConstraints(0);
        tableGridBagConstraints.fill = GridBagConstraints.BOTH;
        tableGridBagConstraints.weightx = 1;
        tableGridBagConstraints.weighty = 1;
        add(new JScrollPane(table), tableGridBagConstraints);

        directoryInput.setPath(config.getDefaultDownloadDir());
        add(directoryInput, getConstraints(1));

        add(statusLabel, getConstraints(2));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        add(buttonsPanel, getConstraints(3));

        handler.setDirectoryValidationListener(directoryInput::handleValidationEvent);
        handler.setFetchStateListener(this::setFetchState);
        handler.setItemStatusListener(table::setStatus);
        handler.setItemsListener(this::setItems);
        handler.setErrorListener(this::showError);
        handler.setFinishedListener(this::showFinished);

        startButton.addActionListener(event -> handler.startDownload(directoryInput.getText(), table.getItems()));
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

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    private void showFinished() {
        JOptionPane.showMessageDialog(this, "Zakończono pobieranie", "Komunikat", JOptionPane.INFORMATION_MESSAGE);
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
