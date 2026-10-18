package swingarchivizer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

final class AppConfig {

    String getTitle() {
        return "Archivizer v1.0";
    }

    LocalDate getDefaultFromDate() {
        return LocalDate.of(2026, 1, 1);
    }

    LocalDate getDefaultToDate() {
        return LocalDate.of(2026, 1, 31);
    }

    //TODO: a bit misleading
    Path getDefaultDownloadDir() {
        return Paths.get(System.getProperty("user.dir"), "download");
    }
}
