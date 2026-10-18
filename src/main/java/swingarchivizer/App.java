package swingarchivizer;

import swingarchivizer.download.DownloadHandler;
import swingarchivizer.download.DownloadHandlerImpl;
import swingarchivizer.fetchitemsinfo.FetchItemsInfoHandler;
import swingarchivizer.fetchitemsinfo.FetchItemsInfoHandlerImpl;
import swingarchivizer.service.FetchService;
import swingarchivizer.service.HttpService;
import swingarchivizer.ui.UiBlocker;

import javax.swing.*;

class App {
    public static void main(String[] args) {
        HttpService httpService = new HttpService();
        AppConfig config = new AppConfig();
        UiBlocker uiBlocker = new UiBlocker();
        FetchService fetchService = new FetchService(httpService, uiBlocker);
        DownloadHandler downloadHandler = new DownloadHandlerImpl(fetchService, uiBlocker);
        FetchItemsInfoHandler fetchItemsInfoHandler = new FetchItemsInfoHandlerImpl(fetchService, uiBlocker, downloadHandler::loadItems);

        SwingUtilities.invokeLater(() -> {
            AppFrame frame = new AppFrame(
                    config,
                    uiBlocker,
                    fetchItemsInfoHandler,
                    downloadHandler
            );
            frame.setVisible(true);
        });
    }
}
