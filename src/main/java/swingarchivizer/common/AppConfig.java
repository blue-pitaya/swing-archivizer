package swingarchivizer.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public final class AppConfig {

    public String getTitle() {
        return "Archivizer v1.0";
    }

    public LocalDate getDefaultFromDate() {
        return LocalDate.of(2026, 1, 1);
    }

    public LocalDate getDefaultToDate() {
        return LocalDate.of(2026, 1, 31);
    }

    //TODO: a bit misleading
    public Path getDefaultDownloadDir() {
        return Paths.get(System.getProperty("user.dir"), "download");
    }
}
