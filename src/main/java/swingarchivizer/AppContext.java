package swingarchivizer;

import swingarchivizer.dto.FetchItem;

import java.util.ArrayList;
import java.util.List;

public final class AppContext {
    private final Observable<List<FetchItem>> items = new Observable<>(new ArrayList<>());
    private final Observable<Boolean> uiBusy = new Observable<>(false);
    private final Observable<FetchState> fetchState = new Observable<>(FetchState.NEW);

    public AppContext() {
    }

    public Observable<List<FetchItem>> getItems() {
        return items;
    }

    public Observable<Boolean> getUiBusy() {
        return uiBusy;
    }

    public Observable<FetchState> getFetchState() {
        return fetchState;
    }
}
