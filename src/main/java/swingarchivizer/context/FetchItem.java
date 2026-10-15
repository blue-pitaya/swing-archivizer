package swingarchivizer.context;

import swingarchivizer.dto.Item;
import swingarchivizer.util.Observable;

public final class FetchItem {
    public enum Status {
        QUEUED("W kolejce"),
        DOWNLOADING("Pobieranie..."),
        DONE("Zakończono"),
        ERROR("Błąd");

        public final String label;

        Status(String label) {
            this.label = label;
        }
    }

    public Item value;
    public Observable<Status> status;

    public FetchItem(Item value) {
        this.value = value;
        this.status = new Observable<>(Status.QUEUED);
    }
}
