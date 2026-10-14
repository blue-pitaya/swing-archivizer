package swingarchivizer;

public enum FetchState {
    NEW("Gotowy"),
    RUNNING("Pobieranie w trakcie..."),
    STOPPED("Zatrzymano pobieranie");

    private final String label;

    FetchState(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
