package swingarchivizer;

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

    public HttpService.Item value;
    public Status status;

    public FetchItem(HttpService.Item value) {
        this.value = value;
        this.status = Status.QUEUED;
    }
}
