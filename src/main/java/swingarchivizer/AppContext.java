package swingarchivizer;

import swingarchivizer.dto.Item;

import java.util.ArrayList;
import java.util.List;

public final class AppContext {
    private final Observable<List<Item>> items;
    private final Observable<Boolean> uiBusy;

    public AppContext() {
        items = new Observable<>(new ArrayList<>());
        uiBusy = new Observable<>(false);
    }

    public Observable<List<Item>> getItems() {
        return items;
    }

    public Observable<Boolean> getUiBusy() {
        return uiBusy;
    }
}
