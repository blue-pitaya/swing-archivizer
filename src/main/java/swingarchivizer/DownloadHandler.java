package swingarchivizer;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

interface DownloadHandler {

    void startDownload(String directory, List<FetchItem> items);

    void setDirectoryValidationListener(Consumer<ValidationEvent> listener);

    void setFetchStateListener(Consumer<DownloadPanel.FetchState> listener);

    void setItemStatusListener(BiConsumer<FetchItem, FetchItem.Status> listener);

    void setItemsListener(Consumer<List<FetchItem>> listener);

    void setErrorListener(Consumer<Exception> listener);

    void setFinishedListener(Runnable listener);
}
