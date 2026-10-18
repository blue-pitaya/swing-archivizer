package swingarchivizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FetchItemsInfoFormPanelTest {

    @Test
    void busyDisablesAllComponents() throws Exception {
        List<Boolean> enabled = new ArrayList<>();

        SwingUtilities.invokeAndWait(() -> {
            FetchItemsInfoFormPanel panel = new FetchItemsInfoFormPanel(new AppConfig(), new FakeHandler());
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
