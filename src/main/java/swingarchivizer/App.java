package swingarchivizer;

import swingarchivizer.context.AppContext;
import swingarchivizer.presenters.DownloadPresenter;
import swingarchivizer.presenters.FetchItemsInfoFormPresenter;
import swingarchivizer.views.DownloadPanel;
import swingarchivizer.views.FetchItemsInfoFormPanel;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        AppContext ctx = new AppContext();
        HttpService httpService = new HttpService();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Archivizer v1.0");
            MessageDialogHelper messageDialogHelper = new MessageDialogHelper(frame);

            FetchItemsInfoFormPanel fetchItemsInfoFormPanel = new FetchItemsInfoFormPanel();
            new FetchItemsInfoFormPresenter(fetchItemsInfoFormPanel, ctx, messageDialogHelper, httpService);
            frame.add(fetchItemsInfoFormPanel.getComponent(), BorderLayout.NORTH);

            DownloadPanel downloadPanel = new DownloadPanel();
            new DownloadPresenter(downloadPanel, ctx, messageDialogHelper, httpService);
            frame.add(downloadPanel.getComponent(), BorderLayout.CENTER);

            frame.setMinimumSize(new Dimension(100, 200));
            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
