package swingarchivizer;

import swingarchivizer.components.DownloadComponent;
import swingarchivizer.components.FirstForm;
import swingarchivizer.context.AppContext;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        AppContext ctx = new AppContext();
        HttpService httpService = new HttpService();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Archivizer v1.0");
            MessageDialogHelper messageDialogHelper = new MessageDialogHelper(frame);

            FirstForm firstForm = new FirstForm(ctx, messageDialogHelper, httpService);
            frame.add(firstForm.getComponent(), BorderLayout.NORTH);

            DownloadComponent downloadComponent = new DownloadComponent(ctx, messageDialogHelper, httpService);
            frame.add(downloadComponent.getComponent(), BorderLayout.CENTER);

            frame.setMinimumSize(new Dimension(100, 200));
            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
