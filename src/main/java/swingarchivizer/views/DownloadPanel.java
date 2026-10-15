package swingarchivizer.views;

import swingarchivizer.components.UniDirectoryInput;
import swingarchivizer.context.FetchItem;
import swingarchivizer.presenters.DownloadPresenter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public final class DownloadPanel implements DownloadPresenter.View {

    private final JPanel container = new JPanel(new GridBagLayout());
    private final FetchItemsTableModel tableModel = new FetchItemsTableModel();
    private final JTable table = getTable(tableModel);
    private final UniDirectoryInput directoryInput = new UniDirectoryInput("Katalog docelowy");
    private final JLabel statusLabel = new JLabel();
    private final JButton startButton = new JButton("Rozpocznij");
    private final JButton stopButton = new JButton("Zatrzymaj");

    public DownloadPanel() {
        GridBagConstraints tableGridBagConstraints = getConstraints(0);
        tableGridBagConstraints.fill = GridBagConstraints.BOTH;
        tableGridBagConstraints.weightx = 1;
        tableGridBagConstraints.weighty = 1;
        container.add(new JScrollPane(table), tableGridBagConstraints);

        directoryInput.setPath(Paths.get(System.getProperty("user.dir"), "download"));
        container.add(directoryInput, getConstraints(1));

        container.add(statusLabel, getConstraints(2));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        container.add(buttonsPanel, getConstraints(3));
    }

    @Override
    public void addStartListener(Runnable listener) {
        startButton.addActionListener(event -> listener.run());
    }

    @Override
    public Optional<Path> getDownloadDir() {
        return directoryInput.getValue();
    }

    @Override
    public void setItems(List<FetchItem> items) {
        tableModel.setItems(items);
    }

    @Override
    public void setTableEnabled(boolean enabled) {
        table.setEnabled(enabled);
    }

    @Override
    public void setDirectoryEnabled(boolean enabled) {
        directoryInput.setEnabled(enabled);
    }

    @Override
    public void setStartEnabled(boolean enabled) {
        startButton.setEnabled(enabled);
    }

    @Override
    public void setStopEnabled(boolean enabled) {
        stopButton.setEnabled(enabled);
    }

    @Override
    public void setStatusText(String text) {
        statusLabel.setText(text);
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
