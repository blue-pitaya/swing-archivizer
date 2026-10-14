package swingarchivizer;

import swingarchivizer.dto.Item;

import java.util.ArrayList;
import java.util.List;

public final class AppContext {
    private final Observable<List<Item>> items;

    public AppContext() {
        items = new Observable<>(new ArrayList<>());
    }

    public Observable<List<Item>> getItems() {
        return items;
    }
}
