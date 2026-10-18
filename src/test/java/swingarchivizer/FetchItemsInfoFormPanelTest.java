package swingarchivizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FetchItemsInfoFormPanelTest {

    @Test
    void busyDisablesAllComponents() throws Exception {
        List<Boolean> enabled = new ArrayList<>();

        SwingUtilities.invokeAndWait(() -> {
            FetchItemsInfoFormPanel panel = new FetchItemsInfoFormPanel(new FakeDatesProvider(), new FakeHandler());
            panel.setBusy(true);
            for (Component component : panel.getComponents()) {
                enabled.add(component.isEnabled());
            }
        });

        assertEquals(3, enabled.size());
        enabled.forEach(Assertions::assertFalse);
    }

    private static final class FakeDatesProvider implements DefaultDatesProvider {
        @Override
        public String getFromDate() {
            return "2026-01-01";
        }

        @Override
        public String getToDate() {
            return "2026-01-31";
        }
    }

    private static final class FakeHandler implements FetchItemsInfoHandler {
        @Override
        public void submitForm(String from, String to) {
        }

        @Override
        public void setFromValidationListener(Consumer<ValidationEvent> listener) {
        }

        @Override
        public void setToValidationListener(Consumer<ValidationEvent> listener) {
        }
    }
}
