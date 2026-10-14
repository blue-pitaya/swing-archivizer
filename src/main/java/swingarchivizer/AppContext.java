package swingarchivizer;

import swingarchivizer.dto.Item;

public final class AppContext {
    private Item[] items;

    public AppContext() {
    }

    public void setItems(Item[] items) {
        this.items = items;
    }
}
