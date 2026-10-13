package swingarchivizer;

import javax.swing.*;
import java.awt.*;

public final class UniTextInput extends JPanel {
    private final JTextField field;

    public UniTextInput(String text) {
        super(new BorderLayout(0, 4));
        JLabel label = new JLabel(text);
        field = new JTextField(20);
        label.setLabelFor(field);
        add(label, BorderLayout.NORTH);
        add(field, BorderLayout.CENTER);
    }

    public String getValue() {
        return field.getText();
    }

    public void setValue(String value) {
        field.setText(value);
    }

    public JTextField getField() {
        return field;
    }
}
