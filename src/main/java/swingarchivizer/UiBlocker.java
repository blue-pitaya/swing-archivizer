package swingarchivizer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

final class UiBlocker {

    enum Mode {
        FETCH,
        DOWNLOAD
    }

    private final Map<BusyAware, Set<Mode>> panels = new LinkedHashMap<>();
    private final Set<Mode> activeModes = EnumSet.noneOf(Mode.class);

    void add(BusyAware panel, Mode... modes) {
        Set<Mode> panelModes = EnumSet.noneOf(Mode.class);
        Collections.addAll(panelModes, modes);
        panels.put(panel, panelModes);
    }

    BusyAware forMode(Mode mode) {
        return busy -> setBusy(mode, busy);
    }

    private void setBusy(Mode mode, boolean busy) {
        if (busy) {
            activeModes.add(mode);
        } else {
            activeModes.remove(mode);
        }

        for (Map.Entry<BusyAware, Set<Mode>> entry : panels.entrySet()) {
            if (entry.getValue().contains(mode)) {
                entry.getKey().setBusy(!Collections.disjoint(entry.getValue(), activeModes));
            }
        }
    }
}
