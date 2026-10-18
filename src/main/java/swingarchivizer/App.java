package swingarchivizer;

import swingarchivizer.common.AppConfig;
import swingarchivizer.common.FetchService;
import swingarchivizer.common.HttpService;
import swingarchivizer.common.UiBlocker;
import swingarchivizer.download.DownloadHandlerImpl;
import swingarchivizer.fetchitemsinfo.FetchItemsInfoHandler;
import swingarchivizer.fetchitemsinfo.FetchItemsInfoHandlerImpl;

import javax.swing.*;

class App {
    public static void main(String[] args) {
        HttpService httpService = new HttpService();
        AppConfig config = new AppConfig();
        UiBlocker uiBlocker = new UiBlocker();
        FetchService fetchService = new FetchService(httpService, uiBlocker);
        DownloadHandlerImpl downloadHandler = new DownloadHandlerImpl(fetchService, uiBlocker);
        FetchItemsInfoHandler fetchItemsInfoHandler = new FetchItemsInfoHandlerImpl(fetchService, uiBlocker, downloadHandler::showItems);

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
