package swingarchivizer;

import java.util.List;

interface DownloadHandler {

    void startDownload(String directory, List<FetchItem> items);

    void setListener(DownloadListener listener);
}
