package swingarchivizer;

import javax.swing.*;
import java.awt.*;

final class AppFrame extends JFrame implements Notifier {

    AppFrame(
            HttpService httpService,
            AppConfig config,
            UiBlocker uiBlocker
    ) {
        super(config.getTitle());

        BusyAware fetchBlocker = uiBlocker.forMode(UiBlocker.Mode.FETCH);
        FetchService fetchService = new FetchService(this, httpService, fetchBlocker);

        DownloadPanel downloadPanel = new DownloadPanel(this, fetchService, uiBlocker.forMode(UiBlocker.Mode.DOWNLOAD));
        uiBlocker.add(downloadPanel, UiBlocker.Mode.FETCH);
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
        FetchItemsInfoHandler fetchItemsInfoHandler = new FetchItemsInfoHandlerImpl(fetchService, fetchBlocker, items -> {
            downloadPanel.setItems(items);
            downloadPanel.setFetchState(DownloadPanel.FetchState.NEW);
        });
        FetchItemsInfoFormPanel fetchItemsInfoFormPanel = new FetchItemsInfoFormPanel(defaultDatesProvider, fetchItemsInfoHandler);
        uiBlocker.add(fetchItemsInfoFormPanel, UiBlocker.Mode.FETCH, UiBlocker.Mode.DOWNLOAD);
        add(fetchItemsInfoFormPanel, BorderLayout.NORTH);

        setMinimumSize(new Dimension(100, 200));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void report(Exception e) {
        JOptionPane.showMessageDialog(this, e, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Komunikat", JOptionPane.INFORMATION_MESSAGE);
    }
}
