package swingarchivizer.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.SwingUtilities;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniDirectoryInputTest {

    @TempDir
    Path directory;

    @Test
    void getValueReturnsExistingDirectory() throws Exception {
        AtomicReference<Optional<Path>> value = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            UniDirectoryInput directoryInput = new UniDirectoryInput("Katalog");
            directoryInput.setPath(directory);
            value.set(directoryInput.getValue());
        });

        assertEquals(Optional.of(directory), value.get());
    }
}
