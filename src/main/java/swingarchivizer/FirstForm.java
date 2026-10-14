package swingarchivizer;

import swingarchivizer.dto.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public final class FirstForm {

    private final AppContext ctx;
    private final ErrorReporter errorReporter;

    private final JPanel form = new JPanel(new GridBagLayout());
    private final UniDateInput dateFormInput = new UniDateInput("Data początkowa");
    private final UniDateInput dateToInput = new UniDateInput("Data końcowa");
    private final JButton confirmButton = new JButton("Pobierz");

    public FirstForm(AppContext ctx, ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
        this.ctx = ctx;

        dateFormInput.getInput().setText("2026-01-01");
        dateToInput.getInput().setText("2026-01-31");
        confirmButton.addActionListener(this::confirm);

        ctx.getUiBusy().subscribeAndCall(uiBusy -> updateForm());
        ctx.getFetchState().subscribeAndCall(fetchState -> updateForm());

        form.add(dateFormInput, getConstraints(0, 0));
        form.add(dateToInput, getConstraints(1, 0));
        form.add(confirmButton, getConstraints(0, 1));
    }

    private void updateForm() {
        boolean enabled = !ctx.getUiBusy().getValue() && ctx.getFetchState().getValue() != FetchState.RUNNING;
        dateFormInput.setEnabled(enabled);
        dateToInput.setEnabled(enabled);
        confirmButton.setEnabled(enabled);
    }

    public JPanel getComponent() {
        return form;
    }

    private void confirm(ActionEvent event) {
        Optional<LocalDate> from = dateFormInput.getValue();
        Optional<LocalDate> to = dateToInput.getValue();
        if (!from.isPresent() || !to.isPresent()) {
            return;
        }

        ctx.getUiBusy().setValue(true);
        new SwingWorker<List<Item>, Void>() {
            @Override
            protected List<Item> doInBackground() throws IOException {
                return Arrays.asList(new HttpService().postItemsIds(from.get(), to.get()));
            }

            @Override
            protected void done() {
                try {
                    ctx.getItems().setValue(get());
                } catch (InterruptedException | ExecutionException e) {
                    errorReporter.report(e);
                } finally {
                    ctx.getUiBusy().setValue(false);
                }
            }
        }.execute();
    }

    private static GridBagConstraints getConstraints(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.LINE_START;

        return c;
    }
}
