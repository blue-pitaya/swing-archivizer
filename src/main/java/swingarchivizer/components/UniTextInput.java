package swingarchivizer.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class UniTextInput extends JPanel {
    private static final int GAP = 4;
    private static final int VALIDATION_GAP = 0;
    private static final String EMPTY_VALIDATION_MESSAGE = " ";

    private final Border defaultBorder;
    private final Border errorBorder;

    private final JTextField field;
    private final JLabel label;
    private final JLabel validationMessage;
    private String placeholder;

    public UniTextInput(String labelText) {
        super(new GridBagLayout());

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

        defaultBorder = field.getBorder();

        Insets i = defaultBorder.getBorderInsets(field);
        errorBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(i.top - 1, i.left - 1, i.bottom - 1, i.right - 1)
        );

        label = new JLabel(labelText);
        label.setLabelFor(field);

        validationMessage = new JLabel(EMPTY_VALIDATION_MESSAGE);
        validationMessage.setForeground(Color.RED);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, GAP, 0);
        c.anchor = GridBagConstraints.LINE_START;
        add(label, c);

        c.gridy = 1;
        c.insets = new Insets(0, 0, VALIDATION_GAP, 0);
        add(field, c);

        c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 0);
        add(validationMessage, c);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        label.setEnabled(enabled);
        field.setEnabled(enabled);
    }

    public void setPlaceholder(String v) {
        placeholder = v;
    }

    public void setValidationMessage(String v) {
        if (v == null || v.isEmpty()) {
            clearValidationMessage();
        }
        validationMessage.setText(v);
        field.setBorder(errorBorder);
    }

    public void clearValidationMessage() {
        validationMessage.setText(EMPTY_VALIDATION_MESSAGE);
        field.setBorder(defaultBorder);
    }

    public void setText(String v) {
        field.setText(v);
    }

    public String getText() {
        return field.getText();
    }
}
