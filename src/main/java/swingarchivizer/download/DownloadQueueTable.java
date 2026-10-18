package swingarchivizer.download;

import swingarchivizer.service.FetchItem;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

final class DownloadQueueTable extends JTable {

    private final Model tableModel = new Model();

    DownloadQueueTable() {
        setModel(tableModel);

        setPreferredScrollableViewportSize(new Dimension(400, 200));
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setEnabled(table.isEnabled());
                return c;
            }
        });
    }

    void setItems(List<FetchItem> items) {
        tableModel.setItems(items);
    }

    List<FetchItem> getItems() {
        return tableModel.items;
    }

    void setStatus(FetchItem item, FetchItem.Status status) {
        item.status = status;
        int row = tableModel.items.indexOf(item);
        tableModel.fireTableRowsUpdated(row, row);
    }

    private static final class Model extends AbstractTableModel {
        private static final String[] COLUMNS = {"ID", "Kod", "Data", "Status"};

        private List<FetchItem> items = new ArrayList<>();

        void setItems(List<FetchItem> items) {
            this.items = items;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            FetchItem item = items.get(row);
            switch (column) {
                case 0:
                    return item.id;
                case 1:
                    return item.code;
                case 2:
                    return item.date;
                case 3:
                    return getStatusLabel(item.status);
                default:
                    throw new IllegalArgumentException("Unknown column " + column);
            }
        }

        private static String getStatusLabel(FetchItem.Status status) {
            switch (status) {
                case QUEUED:
                    return "W kolejce";
                case DOWNLOADING:
                    return "Pobieranie...";
                case DONE:
                    return "Zakończono";
                case ERROR:
                    return "Błąd";
                default:
                    throw new IllegalArgumentException("Unknown status " + status);
            }
        }
    }
}
