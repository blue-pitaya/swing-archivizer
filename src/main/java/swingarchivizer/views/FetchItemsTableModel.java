package swingarchivizer.views;

import swingarchivizer.context.FetchItem;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class FetchItemsTableModel extends AbstractTableModel {
    private static final String[] COLUMNS = {"ID", "Kod", "Data", "Status"};

    private List<FetchItem> items = new ArrayList<>();
    private final List<Consumer<FetchItem.Status>> statusListeners = new ArrayList<>();

    void setItems(List<FetchItem> items) {
        for (int row = 0; row < this.items.size(); row++) {
            this.items.get(row).status.unsubscribe(statusListeners.get(row));
        }
        statusListeners.clear();

        this.items = items;
        for (int row = 0; row < items.size(); row++) {
            int statusRow = row;
            Consumer<FetchItem.Status> listener = status -> fireTableRowsUpdated(statusRow, statusRow);
            items.get(row).status.subscribe(listener);
            statusListeners.add(listener);
        }
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
                return item.value.id;
            case 1:
                return item.value.code;
            case 2:
                return item.value.date;
            case 3:
                return item.status.getValue().label;
            default:
                throw new IllegalArgumentException("Unknown column " + column);
        }
    }
}
