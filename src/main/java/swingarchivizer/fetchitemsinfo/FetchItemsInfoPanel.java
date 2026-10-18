package swingarchivizer.fetchitemsinfo;

import swingarchivizer.ui.BusyListener;
import swingarchivizer.ui.UniTextInput;
import swingarchivizer.ui.ValidationEvent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public final class FetchItemsInfoPanel extends JPanel implements BusyListener {

    private final UniTextInput dateFormInput = new UniTextInput("Data początkowa");
    private final UniTextInput dateToInput = new UniTextInput("Data końcowa");
    private final JButton confirmButton = new JButton("Pobierz");

    public FetchItemsInfoPanel(
            LocalDate defaultFromDate,
            LocalDate defaultToDate,
            FetchItemsInfoHandler handler
    ) {
        super(new GridBagLayout());

        dateFormInput.setPlaceholder("YYYY-MM-DD");
        dateFormInput.setText(defaultFromDate.toString());

        dateToInput.setPlaceholder("YYYY-MM-DD");
        dateToInput.setText(defaultToDate.toString());

        handler.setListener(new FetchItemsInfoListener() {
            @Override
            public void fromValidated(ValidationEvent event) {
                dateFormInput.handleValidationEvent(event);
            }

            @Override
            public void toValidated(ValidationEvent event) {
                dateToInput.handleValidationEvent(event);
            }

            @Override
            public void errorOccurred(Exception e) {
                showError(e);
            }
        });

        confirmButton.addActionListener(e ->
                handler.submitForm(dateFormInput.getText(), dateToInput.getText())
        );

        add(dateFormInput, getConstraints(0, 0));
        add(dateToInput, getConstraints(1, 0));
        add(confirmButton, getConstraints(0, 1));
    }

    @Override
    public void busyChanged(boolean busy) {
        dateFormInput.setEnabled(!busy);
        dateToInput.setEnabled(!busy);
        confirmButton.setEnabled(!busy);
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e, "Błąd", JOptionPane.ERROR_MESSAGE);
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
