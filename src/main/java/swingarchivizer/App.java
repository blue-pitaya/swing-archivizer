package swingarchivizer;

import swingarchivizer.context.AppContext;
import swingarchivizer.views.DownloadPanel;
import swingarchivizer.views.FirstFormPanel;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        AppContext ctx = new AppContext();
        HttpService httpService = new HttpService();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Archivizer v1.0");
            MessageDialogHelper messageDialogHelper = new MessageDialogHelper(frame);

            FirstFormPanel firstFormPanel = new FirstFormPanel(ctx, messageDialogHelper, httpService);
            frame.add(firstFormPanel.getComponent(), BorderLayout.NORTH);

            DownloadPanel downloadPanel = new DownloadPanel(ctx, messageDialogHelper, httpService);
            frame.add(downloadPanel.getComponent(), BorderLayout.CENTER);

            frame.setMinimumSize(new Dimension(100, 200));
            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
