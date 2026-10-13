package swingarchivizer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public final class ItemsTable {
    private final JScrollPane pane;

    public ItemsTable() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Status"}, 10) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));

        pane = new JScrollPane(table);
    }

    public JScrollPane getPane() {
        return pane;
    }
}
