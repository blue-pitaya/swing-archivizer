package swingarchivizer;

import swingarchivizer.common.AppConfig;
import swingarchivizer.common.UiBlocker;
import swingarchivizer.download.DownloadHandler;
import swingarchivizer.download.DownloadPanel;
import swingarchivizer.fetchitemsinfo.FetchItemsInfoPanel;
import swingarchivizer.fetchitemsinfo.FetchItemsInfoHandler;

import javax.swing.*;
import java.awt.*;

final class AppFrame extends JFrame {

    AppFrame(
            AppConfig config,
            UiBlocker uiBlocker,
            FetchItemsInfoHandler fetchItemsInfoHandler,
            DownloadHandler downloadHandler
    ) {
        super(config.getTitle());

        DownloadPanel downloadPanel = new DownloadPanel(config, downloadHandler);
        uiBlocker.add(downloadPanel, UiBlocker.Mode.FETCH);
        add(downloadPanel, BorderLayout.CENTER);

        FetchItemsInfoPanel fetchItemsInfoPanel = new FetchItemsInfoPanel(config, fetchItemsInfoHandler);
        uiBlocker.add(fetchItemsInfoPanel, UiBlocker.Mode.FETCH, UiBlocker.Mode.DOWNLOAD);
        add(fetchItemsInfoPanel, BorderLayout.NORTH);

        setMinimumSize(new Dimension(100, 200));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
