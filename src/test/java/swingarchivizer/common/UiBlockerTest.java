package swingarchivizer.common;

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

        uiBlocker.setBusy(UiBlocker.Mode.FETCH, true);

        assertEquals(Collections.singletonList(true), first);
        assertEquals(Collections.singletonList(true), second);
    }

    @Test
    void panelStaysBusyWhileAnyOfItsModesIsBusy() {
        List<Boolean> busy = new ArrayList<>();
        UiBlocker uiBlocker = new UiBlocker();
        uiBlocker.add(busy::add, UiBlocker.Mode.FETCH, UiBlocker.Mode.DOWNLOAD);

        uiBlocker.setBusy(UiBlocker.Mode.FETCH, true);
        uiBlocker.setBusy(UiBlocker.Mode.DOWNLOAD, true);
        uiBlocker.setBusy(UiBlocker.Mode.FETCH, false);

        assertEquals(Arrays.asList(true, true, true), busy);
    }
}
