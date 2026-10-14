package swingarchivizer;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        AppContext ctx = new AppContext();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Archivizer v1.0");
            ErrorReporter er = new ErrorReporter(frame);

            FirstForm firstForm = new FirstForm(ctx, er);
            frame.add(firstForm.getComponent(), BorderLayout.NORTH);

            ItemsTable itemsTable = new ItemsTable();
            frame.add(itemsTable.getComponent(), BorderLayout.CENTER);

            frame.setMinimumSize(new Dimension(100, 200));
            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
