package swingarchivizer.views;

import swingarchivizer.MessageDialogHelper;
import swingarchivizer.HttpService;
import swingarchivizer.components.UniDirectoryInput;
import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchItem;
import swingarchivizer.context.FetchState;
import swingarchivizer.workers.DownloadItemWorker;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class DownloadPanel {

    private final AppContext ctx;
    private final MessageDialogHelper messageDialogHelper;
    private final HttpService httpService;

    private final JPanel container = new JPanel(new GridBagLayout());
    private final FetchItemsTableModel tableModel = new FetchItemsTableModel();
    private final JTable table = getTable(tableModel);
    private final UniDirectoryInput directoryInput = new UniDirectoryInput("Katalog docelowy");
    private final JLabel statusLabel = new JLabel();
    private final JButton startButton = new JButton("Rozpocznij");
    private final JButton stopButton = new JButton("Zatrzymaj");

    public DownloadPanel(AppContext ctx, MessageDialogHelper messageDialogHelper, HttpService httpService) {
        this.messageDialogHelper = messageDialogHelper;
        this.ctx = ctx;
        this.httpService = httpService;

        GridBagConstraints tableGridBagConstraints = getConstraints(0);
        tableGridBagConstraints.fill = GridBagConstraints.BOTH;
        tableGridBagConstraints.weightx = 1;
        tableGridBagConstraints.weighty = 1;
        container.add(new JScrollPane(table), tableGridBagConstraints);

        directoryInput.setPath(Paths.get(System.getProperty("user.dir"), "download"));
        container.add(directoryInput, getConstraints(1));

        startButton.addActionListener(this::start);

        container.add(statusLabel, getConstraints(2));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        container.add(buttonsPanel, getConstraints(3));

        ctx.getItems().subscribeAndCall(items -> SwingUtilities.invokeLater(() -> {
            tableModel.setItems(items);
            updateButtons();
        }));
        ctx.getUiBusy().subscribeAndCall(uiBusy -> {
            table.setEnabled(!uiBusy);
            updateDirectoryInput();
            updateButtons();
        });
        ctx.getFetchState().subscribeAndCall(fetchState -> {
            statusLabel.setText(fetchState.toString());
            updateDirectoryInput();
            updateButtons();
        });
    }

    private void start(ActionEvent event) {
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
        directoryInput.setEnabled(!ctx.getUiBusy().getValue() && ctx.getFetchState().getValue() != FetchState.RUNNING);
    }

    private void updateButtons() {
        if (ctx.getUiBusy().getValue()) {
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
            return;
        }

        boolean anyItems = !ctx.getItems().getValue().isEmpty();
        boolean finished = ctx.getFetchState().getValue() == FetchState.FINISHED;
        startButton.setEnabled(anyItems && !finished && ctx.getFetchState().getValue() != FetchState.RUNNING);
        stopButton.setEnabled(anyItems && !finished && ctx.getFetchState().getValue() != FetchState.NEW);
    }

    public JPanel getComponent() {
        return container;
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
