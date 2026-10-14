package swingarchivizer;

import swingarchivizer.dto.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public final class DownloadComponent {

    private final AppContext ctx;
    private final ErrorReporter errorReporter;

    private final JPanel container = new JPanel(new GridBagLayout());
    private final DefaultTableModel tableModel = getTableModel();
    private final JTable table = getTable(tableModel);
    private final UniDirectoryInput directoryInput = new UniDirectoryInput("Katalog docelowy");
    private final JLabel statusLabel = new JLabel();
    private final JButton startButton = new JButton("Rozpocznij");
    private final JButton stopButton = new JButton("Zatrzymaj");

    public DownloadComponent(AppContext ctx, ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
        this.ctx = ctx;

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
            tableModel.setRowCount(0);
            for (Item item : items) {
                tableModel.addRow(new Object[]{item.id, item.code, item.date, "W kolejce"});
            }
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
        //new SwingWorker<List<Item>, Void>() {
        //    @Override
        //    protected List<Item> doInBackground() throws IOException {
        //        return Arrays.asList(new HttpService().postItemsIds(from.get(), to.get()));
        //    }

        //    @Override
        //    protected void done() {
        //        try {
        //            ctx.getItems().setValue(get());
        //        } catch (InterruptedException | ExecutionException e) {
        //            errorReporter.report(e);
        //        } finally {
        //            ctx.getUiBusy().setValue(false);
        //        }
        //    }
        //}.execute();
    }

    private void updateDirectoryInput() {
        directoryInput.setEnabled(!ctx.getUiBusy().getValue() && ctx.getFetchState().getValue() != FetchState.RUNNING);
    }

    private void updateButtons() {
        if (ctx.getUiBusy().getValue()) {
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
        }

        boolean anyItems = !ctx.getItems().getValue().isEmpty();
        startButton.setEnabled(anyItems && ctx.getFetchState().getValue() != FetchState.RUNNING);
        stopButton.setEnabled(anyItems && ctx.getFetchState().getValue() != FetchState.NEW);
    }

    public JPanel getComponent() {
        return container;
    }

    private static DefaultTableModel getTableModel() {
        return new DefaultTableModel(new Object[]{"ID", "Kod", "Data", "Status"}, 10) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static JTable getTable(DefaultTableModel tableModel) {
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
