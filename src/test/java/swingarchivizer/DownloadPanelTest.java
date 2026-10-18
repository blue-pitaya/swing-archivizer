package swingarchivizer;

import org.junit.jupiter.api.Test;
import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchItem;
import swingarchivizer.context.FetchState;
import swingarchivizer.dto.Item;

import javax.swing.*;
import java.awt.Component;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DownloadPanelTest {

    private final AppContext ctx = new AppContext();
    private DownloadPanel panel;

    @Test
    void showsFetchStateAsStatusText() throws Exception {
        createPanel();

        ctx.getFetchState().setValue(FetchState.RUNNING);
        flushEdt();

        assertEquals("Pobieranie w trakcie...", statusLabel().getText());
    }

    @Test
    void showsLoadedItemsAndEnablesStart() throws Exception {
        createPanel();

        SwingUtilities.invokeAndWait(() -> panel.setItems(Collections.singletonList(new FetchItem(new Item()))));

        assertEquals(1, table().getRowCount());
        assertTrue(startButton().isEnabled());
        assertFalse(stopButton().isEnabled());
        assertTrue(directoryInput().isEnabled());
    }

    @Test
    void runningDownloadEnablesStopAndLocksDirectory() throws Exception {
        createPanel();

        SwingUtilities.invokeAndWait(() -> panel.setItems(Collections.singletonList(new FetchItem(new Item()))));
        ctx.getFetchState().setValue(FetchState.RUNNING);
        flushEdt();

        assertFalse(startButton().isEnabled());
        assertTrue(stopButton().isEnabled());
        assertFalse(directoryInput().isEnabled());
    }

    @Test
    void busyDisablesTableAndButtons() throws Exception {
        createPanel();

        SwingUtilities.invokeAndWait(() -> panel.setItems(Collections.singletonList(new FetchItem(new Item()))));
        SwingUtilities.invokeAndWait(() -> panel.setBusy(true));

        assertFalse(table().isEnabled());
        assertFalse(startButton().isEnabled());
        assertFalse(stopButton().isEnabled());
        assertFalse(directoryInput().isEnabled());
    }

    private void createPanel() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MessageDialogHelper messageDialogHelper = new MessageDialogHelper(null);
            FetchService fetchService = new FetchService(ctx, messageDialogHelper, new HttpService(), busy -> { });
            panel = new DownloadPanel(ctx, messageDialogHelper, fetchService);
        });
    }

    private static void flushEdt() throws Exception {
        SwingUtilities.invokeAndWait(() -> { });
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
}
