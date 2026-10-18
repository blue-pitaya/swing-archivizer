package swingarchivizer.common;

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

    public int id;
    public String code;
    public String date;
    public Status status;

    public FetchItem(int id, String code, String date) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.status = Status.QUEUED;
    }
}
