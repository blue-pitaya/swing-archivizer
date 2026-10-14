package swingarchivizer;

import swingarchivizer.dto.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public final class DownloadComponent {

    private final AppContext ctx;
    private final ErrorReporter errorReporter;

    private final JPanel container = new JPanel(new GridBagLayout());
    private final DefaultTableModel tableModel = getTableModel();
    private final JTable table = getTable(tableModel);
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

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        container.add(buttonsPanel, getConstraints(1));

        ctx.getItems().subscribeAndCall(items -> SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (Item item : items) {
                tableModel.addRow(new Object[]{item.id, item.code, item.date, "W kolejce"});
            }
            updateButtons();
        }));
        ctx.getUiBusy().subscribeAndCall(uiBusy -> {
            table.setEnabled(!uiBusy);
            updateButtons();
        });
        ctx.getFetchState().subscribeAndCall(fetchState -> updateButtons());
    }

    private void updateButtons() {
        boolean enabled = !ctx.getUiBusy().getValue() && !ctx.getItems().getValue().isEmpty();
        startButton.setEnabled(enabled);
        stopButton.setEnabled(enabled && ctx.getFetchState().getValue() != FetchState.NEW);
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
