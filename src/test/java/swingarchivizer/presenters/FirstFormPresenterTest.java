package swingarchivizer.presenters;

import org.junit.jupiter.api.Test;
import swingarchivizer.HttpService;
import swingarchivizer.MessageDialogHelper;
import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchState;

import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FirstFormPresenterTest {

    private final AppContext ctx = new AppContext();
    private final FakeView view = new FakeView();

    @Test
    void enablesFormWhenIdle() throws Exception {
        createPresenter();

        assertTrue(view.formEnabled);
    }

    @Test
    void disablesFormWhileDownloadIsRunning() throws Exception {
        createPresenter();

        ctx.getFetchState().setValue(FetchState.RUNNING);
        flushEdt();

        assertFalse(view.formEnabled);
    }

    @Test
    void disablesFormWhileUiIsBusy() throws Exception {
        createPresenter();

        ctx.getUiBusy().setValue(true);
        flushEdt();

        assertFalse(view.formEnabled);
    }

    private void createPresenter() throws Exception {
        SwingUtilities.invokeAndWait(() -> new FirstFormPresenter(view, ctx, new MessageDialogHelper(null), new HttpService()));
    }

    private static void flushEdt() throws Exception {
        SwingUtilities.invokeAndWait(() -> { });
    }

    private static final class FakeView implements FirstFormPresenter.View {
        boolean formEnabled;

        @Override
        public void addFetchListener(Runnable listener) {
        }

        @Override
        public Optional<LocalDate> getFrom() {
            return Optional.empty();
        }

        @Override
        public Optional<LocalDate> getTo() {
            return Optional.empty();
        }

        @Override
        public void setFormEnabled(boolean enabled) {
            formEnabled = enabled;
        }
    }
}
