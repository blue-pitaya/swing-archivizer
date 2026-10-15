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

    public void success(String message) {
        JOptionPane.showMessageDialog(frame, message, "Komunikat", JOptionPane.INFORMATION_MESSAGE);
    }
}
