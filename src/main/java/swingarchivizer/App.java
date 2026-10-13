package swingarchivizer;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Archivizer v1.0");
            JPanel form = new JPanel(new GridBagLayout());
            UniTextInput dateFrom = new UniTextInput("Data początkowa");
            UniTextInput dateTo = new UniTextInput("Data końcowa");
            JButton confirm = new JButton("Pobierz");

            GridBagConstraints c1 = new GridBagConstraints();
            c1.gridx = 0;
            c1.gridy = 0;
            c1.insets = new Insets(0, 0, 0, 4);
            form.add(dateFrom, c1);

            GridBagConstraints c2 = new GridBagConstraints();
            c2.gridx = 1;
            c2.gridy = 0;
            form.add(dateTo, c2);

            GridBagConstraints c3 = new GridBagConstraints();
            c3.gridx = 0;
            c3.gridy = 1;
            c3.gridwidth = 2;
            form.add(confirm, c3);

            frame.add(form, BorderLayout.CENTER);

            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
