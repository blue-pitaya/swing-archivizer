package swingarchivizer;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Optional;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Archivizer v1.0");
            JPanel form = new JPanel(new GridBagLayout());

            UniDateInput dateFromInput = new UniDateInput("Data początkowa");
            UniDateInput dateToInput = new UniDateInput("Data końcowa");

            JButton confirmButton = new JButton("Pobierz");
            confirmButton.addActionListener(e -> {
                Optional<LocalDate> from = dateFromInput.getValue();
                Optional<LocalDate> to = dateToInput.getValue();
                if (!from.isPresent() || !to.isPresent()) {
                    return;
                }
                System.out.println(from.get() + " - " + to.get());
            });

            GridBagConstraints c1 = new GridBagConstraints();
            c1.gridx = 0;
            c1.gridy = 0;
            c1.insets = new Insets(4, 4, 4, 4);
            form.add(dateFromInput, c1);

            GridBagConstraints c2 = new GridBagConstraints();
            c2.gridx = 1;
            c2.gridy = 0;
            c2.insets = new Insets(4, 4, 4, 4);
            form.add(dateToInput, c2);

            GridBagConstraints c3 = new GridBagConstraints();
            c3.gridx = 0;
            c3.gridy = 1;
            c3.insets = new Insets(4, 4, 4, 4);
            c3.anchor = GridBagConstraints.LINE_START;
            form.add(confirmButton, c3);

            frame.add(form, BorderLayout.CENTER);

            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
