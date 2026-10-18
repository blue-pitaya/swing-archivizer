package swingarchivizer;

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

        DownloadPanel downloadPanel = new DownloadPanel(downloadHandler);
        uiBlocker.add(downloadPanel, UiBlocker.Mode.FETCH);
        add(downloadPanel, BorderLayout.CENTER);

        FetchItemsInfoFormPanel fetchItemsInfoFormPanel = new FetchItemsInfoFormPanel(config, fetchItemsInfoHandler);
        uiBlocker.add(fetchItemsInfoFormPanel, UiBlocker.Mode.FETCH, UiBlocker.Mode.DOWNLOAD);
        add(fetchItemsInfoFormPanel, BorderLayout.NORTH);

        setMinimumSize(new Dimension(100, 200));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
