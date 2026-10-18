package swingarchivizer.service;

public final class FetchItem {
    public enum Status {
        QUEUED,
        DOWNLOADING,
        DONE,
        ERROR
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
