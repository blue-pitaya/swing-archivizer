package swingarchivizer;

import javax.swing.*;
import java.awt.*;

final class FetchItemsInfoFormPanel extends JPanel implements BusyAware {

    private final UniTextInput dateFormInput = new UniTextInput("Data początkowa");
    private final UniTextInput dateToInput = new UniTextInput("Data końcowa");
    private final JButton confirmButton = new JButton("Pobierz");

    public FetchItemsInfoFormPanel(
            AppConfig config,
            FetchItemsInfoHandler handler
    ) {
        super(new GridBagLayout());

        dateFormInput.setPlaceholder("YYYY-MM-DD");
        dateFormInput.setText(config.getDefaultFromDate().toString());
        handler.setFromValidationListener(dateFormInput::handleValidationEvent);

        dateToInput.setPlaceholder("YYYY-MM-DD");
        dateToInput.setText(config.getDefaultToDate().toString());
        handler.setToValidationListener(dateToInput::handleValidationEvent);
        handler.setErrorListener(this::showError);

        confirmButton.addActionListener(e ->
                handler.submitForm(dateFormInput.getText(), dateToInput.getText())
        );

        add(dateFormInput, getConstraints(0, 0));
        add(dateToInput, getConstraints(1, 0));
        add(confirmButton, getConstraints(0, 1));
    }

    @Override
    public void setBusy(boolean busy) {
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
