package swingarchivizer.presenters;

import org.junit.jupiter.api.Test;
import swingarchivizer.HttpService;
import swingarchivizer.MessageDialogHelper;
import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchItem;
import swingarchivizer.context.FetchState;
import swingarchivizer.dto.Item;

import javax.swing.SwingUtilities;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DownloadPresenterTest {

    private final AppContext ctx = new AppContext();
    private final FakeView view = new FakeView();

    @Test
    void showsFetchStateAsStatusText() throws Exception {
        createPresenter();

        ctx.getFetchState().setValue(FetchState.RUNNING);
        flushEdt();

        assertEquals("Pobieranie w trakcie...", view.statusText);
    }

    @Test
    void passesLoadedItemsToViewAndEnablesStart() throws Exception {
        createPresenter();
        List<FetchItem> items = Collections.singletonList(new FetchItem(new Item()));

        ctx.getItems().setValue(items);
        flushEdt();
        flushEdt();

        assertEquals(items, view.items);
        assertTrue(view.startEnabled);
        assertFalse(view.stopEnabled);
        assertTrue(view.directoryEnabled);
    }

    @Test
    void runningDownloadEnablesStopAndLocksDirectory() throws Exception {
        createPresenter();

        ctx.getItems().setValue(Collections.singletonList(new FetchItem(new Item())));
        ctx.getFetchState().setValue(FetchState.RUNNING);
        flushEdt();
        flushEdt();

        assertFalse(view.startEnabled);
        assertTrue(view.stopEnabled);
        assertFalse(view.directoryEnabled);
    }

    @Test
    void busyUiDisablesTableAndButtons() throws Exception {
        createPresenter();

        ctx.getItems().setValue(Collections.singletonList(new FetchItem(new Item())));
        ctx.getUiBusy().setValue(true);
        flushEdt();
        flushEdt();

        assertFalse(view.tableEnabled);
        assertFalse(view.startEnabled);
        assertFalse(view.stopEnabled);
        assertFalse(view.directoryEnabled);
    }

    private void createPresenter() throws Exception {
        SwingUtilities.invokeAndWait(() -> new DownloadPresenter(view, ctx, new MessageDialogHelper(null), new HttpService()));
    }

    private static void flushEdt() throws Exception {
        SwingUtilities.invokeAndWait(() -> { });
    }

    private static final class FakeView implements DownloadPresenter.View {
        List<FetchItem> items;
        boolean tableEnabled;
        boolean directoryEnabled;
        boolean startEnabled;
        boolean stopEnabled;
        String statusText;

        @Override
        public void addStartListener(Runnable listener) {
        }

        @Override
        public Optional<Path> getDownloadDir() {
            return Optional.empty();
        }

        @Override
        public void setItems(List<FetchItem> items) {
            this.items = items;
        }

        @Override
        public void setTableEnabled(boolean enabled) {
            tableEnabled = enabled;
        }

        @Override
        public void setDirectoryEnabled(boolean enabled) {
            directoryEnabled = enabled;
        }

        @Override
        public void setStartEnabled(boolean enabled) {
            startEnabled = enabled;
        }

        @Override
        public void setStopEnabled(boolean enabled) {
            stopEnabled = enabled;
        }

        @Override
        public void setStatusText(String text) {
            statusText = text;
        }
    }
}
