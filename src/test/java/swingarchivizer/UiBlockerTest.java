package swingarchivizer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UiBlockerTest {

    @Test
    void setBusyIsPassedToAllPanelsOfMode() {
        List<Boolean> first = new ArrayList<>();
        List<Boolean> second = new ArrayList<>();
        UiBlocker uiBlocker = new UiBlocker();
        uiBlocker.add(first::add, UiBlocker.Mode.FETCH);
        uiBlocker.add(second::add, UiBlocker.Mode.FETCH);

        uiBlocker.forMode(UiBlocker.Mode.FETCH).setBusy(true);

        assertEquals(Collections.singletonList(true), first);
        assertEquals(Collections.singletonList(true), second);
    }

    @Test
    void panelStaysBusyWhileAnyOfItsModesIsBusy() {
        List<Boolean> busy = new ArrayList<>();
        UiBlocker uiBlocker = new UiBlocker();
        uiBlocker.add(busy::add, UiBlocker.Mode.FETCH, UiBlocker.Mode.DOWNLOAD);

        uiBlocker.forMode(UiBlocker.Mode.FETCH).setBusy(true);
        uiBlocker.forMode(UiBlocker.Mode.DOWNLOAD).setBusy(true);
        uiBlocker.forMode(UiBlocker.Mode.FETCH).setBusy(false);

        assertEquals(Arrays.asList(true, true, true), busy);
    }
}
