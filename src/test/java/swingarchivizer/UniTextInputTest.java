package swingarchivizer;

import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniTextInputTest {

    @Test
    void getTextReturnsTextSetOnField() throws Exception {
        AtomicReference<String> text = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            UniTextInput textInput = new UniTextInput("Kod");
            textInput.setText("ABC-123");
            text.set(textInput.getText());
        });

        assertEquals("ABC-123", text.get());
    }
}
