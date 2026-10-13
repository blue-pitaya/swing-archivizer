package swingarchivizer;

import javax.swing.*;
import java.awt.*;

public final class UniTextInput extends JPanel {
    private final JTextField field;
    private final JLabel label;
    private final JLabel validationMessage;
    private String placeholder;

    public UniTextInput(String labelText) {
        super(new BorderLayout(0, 4));

        field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (placeholder != null && getText().isEmpty()) {
                    g.setColor(getDisabledTextColor());
                    g.drawString(placeholder, getInsets().left, getBaseline(getWidth(), getHeight()));
                }
            }
        };

        label = new JLabel(labelText);
        label.setLabelFor(field);

        validationMessage = new JLabel("");
        validationMessage.setForeground(Color.RED);

        add(label, BorderLayout.NORTH);
        add(field, BorderLayout.CENTER);
        add(validationMessage, BorderLayout.SOUTH);
    }

    public void setPlaceholder(String v) {
        placeholder = v;
    }

    public void setValidationMessage(String v) {
        validationMessage.setText(v);
    }

    public String getText() {
        return field.getText();
    }
}
