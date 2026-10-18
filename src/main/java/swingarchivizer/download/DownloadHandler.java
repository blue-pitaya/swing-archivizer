package swingarchivizer.download;

import swingarchivizer.FetchItem;

import java.util.List;

public interface DownloadHandler {

    void startDownload(String directory, List<FetchItem> items);

    void setListener(DownloadListener listener);
}
