package swingarchivizer.context;

import swingarchivizer.util.Observable;

import java.util.ArrayList;
import java.util.List;

public final class AppContext {
    private final Observable<List<FetchItem>> items = new Observable<>(new ArrayList<>());
    private final Observable<FetchState> fetchState = new Observable<>(FetchState.NEW);

    public AppContext() {
    }

    public Observable<List<FetchItem>> getItems() {
        return items;
    }

    public Observable<FetchState> getFetchState() {
        return fetchState;
    }
}
