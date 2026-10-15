package swingarchivizer.context;

public enum FetchState {
    NEW("Gotowy"),
    RUNNING("Pobieranie w trakcie..."),
    STOPPED("Zatrzymano pobieranie"),
    FINISHED("Zakończono");

    private final String label;

    FetchState(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
