package swingarchivizer.download;

import swingarchivizer.common.UniTextInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

final class UniDirectoryInput extends UniTextInput {
    private final JButton browseButton = new JButton("Wybierz");

    public UniDirectoryInput(String labelText) {
        super(labelText);

        browseButton.addActionListener(this::browse);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(0, 4, 0, 0);
        add(browseButton, c);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        browseButton.setEnabled(enabled);
    }

    public void setPath(Path path) {
        setText(path.toString());
    }

    public Optional<Path> getValue() {
        Optional<Path> path = toDirectory(getText());
        if (path.isPresent()) {
            clearValidationMessage();
        } else {
            setValidationMessage("Nieprawidłowy katalog");
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
            setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
