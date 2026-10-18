package swingarchivizer;

import javax.swing.*;
import java.awt.*;

final class AppFrame extends JFrame {

    AppFrame(HttpService httpService, AppConfig config) {
        super("Archivizer v1.0");

        MessageDialogHelper messageDialogHelper = new MessageDialogHelper(this);
        UiBlocker uiBlocker = new UiBlocker();
        FetchService fetchService = new FetchService(messageDialogHelper, httpService, uiBlocker);

        DownloadPanel downloadPanel = new DownloadPanel(messageDialogHelper, fetchService);
        uiBlocker.add(downloadPanel);
        add(downloadPanel, BorderLayout.CENTER);

        DefaultDatesProvider defaultDatesProvider = new DefaultDatesProvider() {

            @Override
            public String getFromDate() {
                return config.getDefaultFromDate().toString();
            }

            @Override
            public String getToDate() {
                return config.getDefaultToDate().toString();
            }
        };
        FetchItemsInfoHandler fetchItemsInfoHandler = new FetchItemsInfoHandlerImpl(fetchService, uiBlocker, items -> {
            downloadPanel.setItems(items);
            downloadPanel.setFetchState(DownloadPanel.FetchState.NEW);
        });
        FetchItemsInfoFormPanel fetchItemsInfoFormPanel = new FetchItemsInfoFormPanel(defaultDatesProvider, fetchItemsInfoHandler);
        uiBlocker.add(fetchItemsInfoFormPanel);
        add(fetchItemsInfoFormPanel, BorderLayout.NORTH);

        setMinimumSize(new Dimension(100, 200));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
