package swingarchivizer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//FIXME: make it safe for swing threads
public final class Observable<T> {
    private final List<Consumer<T>> listeners = new ArrayList<>();
    private T value;

    public Observable(T value) {
        this.value = value;
    }

    public void subscribe(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void subscribeAndCall(Consumer<T> listener) {
        listeners.add(listener);
        listener.accept(value);
    }

    public void unsubscribe(Consumer<T> listener) {
        listeners.remove(listener);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        emit(value);
    }

    private void emit(T value) {
        for (Consumer<T> listener : listeners) {
            listener.accept(value);
        }
    }
}
