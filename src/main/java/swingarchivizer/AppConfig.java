package swingarchivizer;

import java.time.LocalDate;

final class AppConfig {

    LocalDate getDefaultFromDate() {
        return LocalDate.of(2026, 1, 1);
    }

    LocalDate getDefaultToDate() {
        return LocalDate.of(2026, 1, 31);
    }
}
