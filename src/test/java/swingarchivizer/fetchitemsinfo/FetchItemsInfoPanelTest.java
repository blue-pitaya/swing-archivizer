package swingarchivizer.fetchitemsinfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FetchItemsInfoPanelTest {

    @Test
    void busyDisablesAllComponents() throws Exception {
        List<Boolean> enabled = new ArrayList<>();

        SwingUtilities.invokeAndWait(() -> {
            FetchItemsInfoPanel panel = new FetchItemsInfoPanel(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31), new FakeHandler());
            panel.busyChanged(true);
            for (Component component : panel.getComponents()) {
                enabled.add(component.isEnabled());
            }
        });

        assertEquals(3, enabled.size());
        enabled.forEach(Assertions::assertFalse);
    }

    private static final class FakeHandler implements FetchItemsInfoHandler {
        @Override
        public void submitForm(String from, String to) {
        }

        @Override
        public void setListener(FetchItemsInfoListener listener) {
        }
    }
}
