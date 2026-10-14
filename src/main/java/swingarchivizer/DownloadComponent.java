package swingarchivizer;

import swingarchivizer.dto.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public final class DownloadComponent {

    private final AppContext ctx;
    private final ErrorReporter errorReporter;

    private final JScrollPane pane;
    private final DefaultTableModel model;
    private final JTable table;

    public DownloadComponent(AppContext ctx, ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
        this.ctx = ctx;

        model = new DefaultTableModel(new Object[]{"ID", "Kod", "Data", "Status"}, 10) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setEnabled(table.isEnabled());
                return c;
            }
        });

        pane = new JScrollPane(table);

        ctx.getItems().subscribe(items -> SwingUtilities.invokeLater(() -> {
            model.setRowCount(0);
            for (Item item : items) {
                model.addRow(new Object[]{item.id, item.code, item.date, "W kolejce"});
            }
        }));
        ctx.getUiBusy().subscribe(uiBusy -> {
            table.setEnabled(!uiBusy);
        });
    }

    public JScrollPane getComponent() {
        return pane;
    }
}
