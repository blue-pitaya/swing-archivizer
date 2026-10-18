package swingarchivizer;

import java.util.ArrayList;
import java.util.List;

final class UiBlocker implements BusyAware {

    private final List<BusyAware> panels = new ArrayList<>();

    void add(BusyAware panel) {
        panels.add(panel);
    }

    @Override
    public void setBusy(boolean busy) {
        for (BusyAware panel : panels) {
            panel.setBusy(busy);
        }
    }
}
