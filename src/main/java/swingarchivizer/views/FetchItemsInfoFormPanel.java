package swingarchivizer.views;

import swingarchivizer.components.UniDateInput;
import swingarchivizer.presenters.FetchItemsInfoFormPresenter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Optional;

public final class FetchItemsInfoFormPanel implements FetchItemsInfoFormPresenter.View {

    private final JPanel form = new JPanel(new GridBagLayout());
    private final UniDateInput dateFormInput = new UniDateInput("Data początkowa");
    private final UniDateInput dateToInput = new UniDateInput("Data końcowa");
    private final JButton confirmButton = new JButton("Pobierz");

    public FetchItemsInfoFormPanel() {
        dateFormInput.getInput().setText("2026-01-01");
        dateToInput.getInput().setText("2026-01-31");

        form.add(dateFormInput, getConstraints(0, 0));
        form.add(dateToInput, getConstraints(1, 0));
        form.add(confirmButton, getConstraints(0, 1));
    }

    @Override
    public void addFetchListener(Runnable listener) {
        confirmButton.addActionListener(event -> listener.run());
    }

    @Override
    public Optional<LocalDate> getFrom() {
        return dateFormInput.getValue();
    }

    @Override
    public Optional<LocalDate> getTo() {
        return dateToInput.getValue();
    }

    @Override
    public void setFormEnabled(boolean enabled) {
        dateFormInput.setEnabled(enabled);
        dateToInput.setEnabled(enabled);
        confirmButton.setEnabled(enabled);
    }

    public JPanel getComponent() {
        return form;
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
