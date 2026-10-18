package swingarchivizer;

import swingarchivizer.components.UniDirectoryInput;
import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchItem;
import swingarchivizer.context.FetchState;
import swingarchivizer.views.FetchItemsTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class DownloadPanel extends JPanel implements BusyAware {

    private final AppContext ctx;
    private final MessageDialogHelper messageDialogHelper;
    private final FetchService fetchService;

    private final FetchItemsTableModel tableModel = new FetchItemsTableModel();
    private final JTable table = getTable(tableModel);
    private final UniDirectoryInput directoryInput = new UniDirectoryInput("Katalog docelowy");
    private final JLabel statusLabel = new JLabel();
    private final JButton startButton = new JButton("Rozpocznij");
    private final JButton stopButton = new JButton("Zatrzymaj");

    private boolean busy;

    public DownloadPanel(
            AppContext ctx,
            MessageDialogHelper messageDialogHelper,
            FetchService fetchService
    ) {
        super(new GridBagLayout());

        this.ctx = ctx;
        this.messageDialogHelper = messageDialogHelper;
        this.fetchService = fetchService;

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
        ctx.getItems().subscribeAndCall(items -> SwingUtilities.invokeLater(() -> {
            tableModel.setItems(items);
            updateButtons();
        }));
        ctx.getFetchState().subscribeAndCall(fetchState -> {
            statusLabel.setText(fetchState.toString());
            updateDirectoryInput();
            updateButtons();
        });
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
        fetchService.downloadItem(item, downloadDir, () -> downloadNext(items, downloadDir));
    }

    private void updateDirectoryInput() {
        directoryInput.setEnabled(!busy &&ctx.getFetchState().getValue() != FetchState.RUNNING);
    }

    private void updateButtons() {
        if (busy) {
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
            return;
        }

        boolean anyItems = !ctx.getItems().getValue().isEmpty();
        boolean finished = ctx.getFetchState().getValue() == FetchState.FINISHED;
        startButton.setEnabled(anyItems && !finished && ctx.getFetchState().getValue() != FetchState.RUNNING);
        stopButton.setEnabled(anyItems && !finished && ctx.getFetchState().getValue() != FetchState.NEW);
    }

    private static JTable getTable(FetchItemsTableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setEnabled(table.isEnabled());
                return c;
            }
        });

        return table;
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
