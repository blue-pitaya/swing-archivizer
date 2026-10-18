package swingarchivizer;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        HttpService httpService = new HttpService();
        AppConfig config = new AppConfig();
        UiBlocker uiBlocker = new UiBlocker();

        SwingUtilities.invokeLater(() -> {
            AppFrame frame = new AppFrame(httpService, config, uiBlocker);
            frame.setVisible(true);
        });
    }
}
