package swingarchivizer.components;

import swingarchivizer.ValidationEvent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public final class UniDateInput extends JPanel {
    private final UniTextInput input;

    public UniDateInput(String labelText) {
        super(new BorderLayout());

        input = new UniTextInput(labelText);
        input.setPlaceholder("YYYY-MM-DD");
        add(input, BorderLayout.CENTER);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        input.setEnabled(enabled);
    }

    public Optional<LocalDate> getValue() {
        try {
            LocalDate date = LocalDate.parse(input.getText());
            input.clearValidationMessage();
            return Optional.of(date);
        } catch (DateTimeParseException e) {
            input.setValidationMessage("Nieprawidłowa data");
            return Optional.empty();
        }
    }

    public void handleValidationEvent(ValidationEvent event) {
        if (event.isOk()) {
            input.clearValidationMessage();
        } else {
            input.setValidationMessage(event.getMessage());
        }
    }

    public UniTextInput getInput() {
        return input;
    }
}
