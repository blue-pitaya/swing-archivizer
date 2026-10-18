package swingarchivizer;

import swingarchivizer.components.UniDateInput;

import javax.swing.*;
import java.awt.*;

public final class FetchItemsInfoFormPanel extends JPanel {

    public FetchItemsInfoFormPanel(
            DefaultDatesProvider defaultDatesProvider,
            FetchItemsInfoHandler handler
    ) {
        super(new GridBagLayout());

        UniDateInput dateFormInput = new UniDateInput("Data początkowa");
        dateFormInput.getInput().setText(defaultDatesProvider.getFromDate());
        handler.setFromValidationListener(dateFormInput::handleValidationEvent);

        UniDateInput dateToInput = new UniDateInput("Data końcowa");
        dateToInput.getInput().setText(defaultDatesProvider.getToDate());
        handler.setToValidationListener(dateToInput::handleValidationEvent);

        JButton confirmButton = new JButton("Pobierz");
        confirmButton.addActionListener(e ->
                handler.submitForm(dateFormInput.getInput().getText(), dateToInput.getInput().getText())
        );

        add(dateFormInput, getConstraints(0, 0));
        add(dateToInput, getConstraints(1, 0));
        add(confirmButton, getConstraints(0, 1));
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
