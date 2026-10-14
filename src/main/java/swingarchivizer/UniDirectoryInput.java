package swingarchivizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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

    public void setPath(Path path) {
        input.setText(path.toString());
    }

    public Optional<Path> getValue() {
        Optional<Path> path = toDirectory(input.getText());
        if (path.isPresent()) {
            input.clearValidationMessage();
        } else {
            input.setValidationMessage("Nieprawidłowy katalog");
        }

        return path;
    }

    private static Optional<Path> toDirectory(String text) {
        if (text.isEmpty()) {
            return Optional.empty();
        }

        try {
            Path path = Paths.get(text);
            return Files.isDirectory(path) ? Optional.of(path) : Optional.empty();
        } catch (InvalidPathException e) {
            return Optional.empty();
        }
    }

    private void browse(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            input.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
