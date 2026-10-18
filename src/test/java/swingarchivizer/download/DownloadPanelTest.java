package swingarchivizer.download;

import org.junit.jupiter.api.Test;
import swingarchivizer.common.AppConfig;
import swingarchivizer.common.FetchItem;

import javax.swing.*;
import java.awt.Component;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DownloadPanelTest {

    private DownloadPanel panel;

    @Test
    void showsFetchStateAsStatusText() throws Exception {
        createPanel();

        SwingUtilities.invokeAndWait(() -> panel.setFetchState(DownloadPanel.FetchState.RUNNING));

        assertEquals("Pobieranie w trakcie...", statusLabel().getText());
    }

    @Test
    void showsLoadedItemsAndEnablesStart() throws Exception {
        createPanel();

        SwingUtilities.invokeAndWait(() -> panel.setItems(Collections.singletonList(new FetchItem(1, "ABC", "2026-01-01"))));

        assertEquals(1, table().getRowCount());
        assertTrue(startButton().isEnabled());
        assertFalse(stopButton().isEnabled());
        assertTrue(directoryInput().isEnabled());
    }

    @Test
    void runningDownloadEnablesStopAndLocksDirectory() throws Exception {
        createPanel();

        SwingUtilities.invokeAndWait(() -> panel.setItems(Collections.singletonList(new FetchItem(1, "ABC", "2026-01-01"))));
        SwingUtilities.invokeAndWait(() -> panel.setFetchState(DownloadPanel.FetchState.RUNNING));

        assertFalse(startButton().isEnabled());
        assertTrue(stopButton().isEnabled());
        assertFalse(directoryInput().isEnabled());
    }

    @Test
    void busyDisablesTableAndButtons() throws Exception {
        createPanel();

        SwingUtilities.invokeAndWait(() -> panel.setItems(Collections.singletonList(new FetchItem(1, "ABC", "2026-01-01"))));
        SwingUtilities.invokeAndWait(() -> panel.busyChanged(true));

        assertFalse(table().isEnabled());
        assertFalse(startButton().isEnabled());
        assertFalse(stopButton().isEnabled());
        assertFalse(directoryInput().isEnabled());
    }

    private void createPanel() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel = new DownloadPanel(new AppConfig(), new FakeHandler()));
    }

    private JTable table() {
        return (JTable) ((JScrollPane) panel.getComponent(0)).getViewport().getView();
    }

    private Component directoryInput() {
        return panel.getComponent(1);
    }

    private JLabel statusLabel() {
        return (JLabel) panel.getComponent(2);
    }

    private JButton startButton() {
        return (JButton) ((JPanel) panel.getComponent(3)).getComponent(0);
    }

    private JButton stopButton() {
        return (JButton) ((JPanel) panel.getComponent(3)).getComponent(1);
    }

    private static final class FakeHandler implements DownloadHandler {
        @Override
        public void startDownload(String directory, List<FetchItem> items) {
        }

        @Override
        public void setListener(DownloadListener listener) {
        }
    }
}
