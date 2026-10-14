package swingarchivizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public final class UniDirectoryInput extends JPanel {
    private final UniTextInput input;
    private final JButton browseButton = new JButton("Wybierz");

    public UniDirectoryInput(String labelText) {
        super(new GridBagLayout());

        input = new UniTextInput(labelText);
        browseButton.addActionListener(this::browse);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(input, c);

        c.gridx = 1;
        c.insets = new Insets(0, 4, 0, 0);
        add(browseButton, c);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        input.setEnabled(enabled);
        browseButton.setEnabled(enabled);
    }

    private void browse(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            input.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
