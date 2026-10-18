package swingarchivizer;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        HttpService httpService = new HttpService();
        AppConfig config = new AppConfig();

        SwingUtilities.invokeLater(() -> {
            AppFrame frame = new AppFrame(httpService, config);
            frame.setVisible(true);
        });
    }
}
