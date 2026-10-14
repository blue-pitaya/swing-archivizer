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

        dateFormInput.getInput().setText("2021-01-01");
        dateToInput.getInput().setText("2021-01-31");

        confirmButton.addActionListener(this::confirm);

        form.add(dateFormInput, getConstraints(0, 0));
        form.add(dateToInput, getConstraints(1, 0));
        form.add(confirmButton, getConstraints(0, 1));
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

        HttpService hs = new HttpService();
        try {
            List<Item> items = Arrays.asList(hs.postItemsIds(from.get(), to.get())); 
            ctx.getItems().setValue(items);
        } catch (IOException e) {
            errorReporter.report(e);
        }
    }

    private GridBagConstraints getConstraints(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.LINE_START;

        return c;
    }
}
