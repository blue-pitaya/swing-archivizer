package swingarchivizer.components;

import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniDateInputTest {

    @Test
    void getValueParsesIsoDate() throws Exception {
        AtomicReference<Optional<LocalDate>> value = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            UniDateInput dateInput = new UniDateInput("Data");
            dateInput.getInput().setText("2026-10-15");
            value.set(dateInput.getValue());
        });

        assertEquals(Optional.of(LocalDate.of(2026, 10, 15)), value.get());
    }
}
