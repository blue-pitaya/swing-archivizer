package swingarchivizer.common;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class UiBlocker {

    public enum Mode {
        FETCH,
        DOWNLOAD
    }

    private final Map<BusyListener, Set<Mode>> panels = new LinkedHashMap<>();
    private final Set<Mode> activeModes = EnumSet.noneOf(Mode.class);

    public void add(BusyListener panel, Mode... modes) {
        Set<Mode> panelModes = EnumSet.noneOf(Mode.class);
        Collections.addAll(panelModes, modes);
        panels.put(panel, panelModes);
    }

    public void setBusy(Mode mode, boolean busy) {
        if (busy) {
            activeModes.add(mode);
        } else {
            activeModes.remove(mode);
        }

        for (Map.Entry<BusyListener, Set<Mode>> entry : panels.entrySet()) {
            if (entry.getValue().contains(mode)) {
                entry.getKey().busyChanged(!Collections.disjoint(entry.getValue(), activeModes));
            }
        }
    }
}
