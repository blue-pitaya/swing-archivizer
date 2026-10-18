package swingarchivizer.download;

import swingarchivizer.FetchItem;
import swingarchivizer.ValidationEvent;

import java.util.List;

interface DownloadListener {

    void directoryValidated(ValidationEvent event);

    void fetchStateChanged(DownloadPanel.FetchState fetchState);

    void itemStatusChanged(FetchItem item, FetchItem.Status status);

    void itemsChanged(List<FetchItem> items);

    void errorOccurred(Exception e);

    void finished();
}
