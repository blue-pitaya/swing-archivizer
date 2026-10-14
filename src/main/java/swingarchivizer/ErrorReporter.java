package swingarchivizer;

import javax.swing.*;

public final class ErrorReporter {
    private final JFrame frame;

    public ErrorReporter(JFrame frame) {
        this.frame = frame;
    }

    public void report(Exception e) {
        JOptionPane.showMessageDialog(frame, e, "Błąd", JOptionPane.ERROR_MESSAGE);
    }
}
