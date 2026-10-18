package swingarchivizer.download;

import swingarchivizer.service.FetchItem;

import java.util.List;

public interface DownloadHandler {

    void startDownload(String directory, List<FetchItem> items);

    void loadItems(List<FetchItem> items);

    void setListener(DownloadListener listener);
}
