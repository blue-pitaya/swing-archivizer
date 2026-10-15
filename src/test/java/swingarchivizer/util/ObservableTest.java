package swingarchivizer.util;

import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObservableTest {

    @Test
    void setValueFromWorkerThreadNotifiesSubscriberOnEdt() throws Exception {
        Observable<String> observable = new Observable<>("initial");
        List<String> received = new ArrayList<>();
        List<Boolean> onEdt = new ArrayList<>();

        SwingUtilities.invokeAndWait(() -> observable.subscribe(value -> {
            received.add(value);
            onEdt.add(SwingUtilities.isEventDispatchThread());
        }));

        observable.setValue("updated");
        SwingUtilities.invokeAndWait(() -> { });

        assertEquals(1, received.size());
        assertEquals("updated", received.get(0));
        assertTrue(onEdt.get(0));
        assertEquals("updated", observable.getValue());
    }
}
