package swingarchivizer.dto;

import java.time.LocalDate;

public final class ItemIdsRequest {

    private final LocalDate from;
    private final LocalDate to;

    public ItemIdsRequest(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from.toString();
    }

    public String getTo() {
        return to.toString();
    }
}
