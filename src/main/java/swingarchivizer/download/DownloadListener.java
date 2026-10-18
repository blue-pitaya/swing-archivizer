package swingarchivizer.download;

import swingarchivizer.common.FetchItem;
import swingarchivizer.common.ValidationEvent;

import java.util.List;

public interface DownloadListener {

    void directoryValidated(ValidationEvent event);

    void fetchStateChanged(DownloadPanel.FetchState fetchState);

    void itemStatusChanged(FetchItem item, FetchItem.Status status);

    void itemsChanged(List<FetchItem> items);

    void errorOccurred(Exception e);

    void finished();
}
