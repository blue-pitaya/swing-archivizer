package swingarchivizer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UiBlockerTest {

    @Test
    void setBusyIsPassedToAllPanels() {
        List<Boolean> first = new ArrayList<>();
        List<Boolean> second = new ArrayList<>();
        UiBlocker uiBlocker = new UiBlocker();
        uiBlocker.add(first::add);
        uiBlocker.add(second::add);

        uiBlocker.setBusy(true);

        assertEquals(Collections.singletonList(true), first);
        assertEquals(Collections.singletonList(true), second);
    }
}
