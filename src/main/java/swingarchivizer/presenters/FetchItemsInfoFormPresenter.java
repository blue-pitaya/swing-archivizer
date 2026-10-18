package swingarchivizer.presenters;

import swingarchivizer.HttpService;
import swingarchivizer.MessageDialogHelper;
import swingarchivizer.context.AppContext;
import swingarchivizer.context.FetchState;
import swingarchivizer.workers.FetchItemsWorker;

import java.time.LocalDate;
import java.util.Optional;

public final class FetchItemsInfoFormPresenter {

    public interface View {
        void addFetchListener(Runnable listener);

        Optional<LocalDate> getFrom();

        Optional<LocalDate> getTo();

        void setFormEnabled(boolean enabled);
    }

    private final View view;
    private final AppContext ctx;
    private final MessageDialogHelper messageDialogHelper;
    private final HttpService httpService;

    public FetchItemsInfoFormPresenter(
            View view,
            AppContext ctx,
            MessageDialogHelper messageDialogHelper,
            HttpService httpService
    ) {
        this.view = view;
        this.ctx = ctx;
        this.messageDialogHelper = messageDialogHelper;
        this.httpService = httpService;

        view.addFetchListener(this::fetch);
        ctx.getUiBusy().subscribeAndCall(uiBusy -> updateForm());
        ctx.getFetchState().subscribeAndCall(fetchState -> updateForm());
    }

    private void updateForm() {
        view.setFormEnabled(!ctx.getUiBusy().getValue() && ctx.getFetchState().getValue() != FetchState.RUNNING);
    }

    private void fetch() {
        Optional<LocalDate> from = view.getFrom();
        Optional<LocalDate> to = view.getTo();
        if (!from.isPresent() || !to.isPresent()) {
            return;
        }

        ctx.getUiBusy().setValue(true);
        new FetchItemsWorker(ctx, messageDialogHelper, httpService, from.get(), to.get()).execute();
    }
}
