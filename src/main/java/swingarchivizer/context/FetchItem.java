package swingarchivizer.context;

import swingarchivizer.dto.Item;

public final class FetchItem {
    public enum Status {
        QUEUED("W kolejce"),
        DOWNLOADING("Pobieranie..."),
        DONE("Zakończono");

        public final String label;

        Status(String label) {
            this.label = label;
        }
    }

    public Item value;
    public Status status;

    public FetchItem(Item value) {
        this.value = value;
        this.status = Status.QUEUED;
    }
}
