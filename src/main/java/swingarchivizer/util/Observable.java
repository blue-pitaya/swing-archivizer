package swingarchivizer.util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Observable<T> {
    private final List<Consumer<T>> listeners = new ArrayList<>();
    private volatile T value;

    public Observable(T value) {
        this.value = value;
    }

    public void subscribe(Consumer<T> listener) {
        requireEventDispatchThread();
        listeners.add(listener);
    }

    public void subscribeAndCall(Consumer<T> listener) {
        requireEventDispatchThread();
        listeners.add(listener);
        listener.accept(value);
    }

    public void unsubscribe(Consumer<T> listener) {
        requireEventDispatchThread();
        listeners.remove(listener);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> setValue(value));
            return;
        }

        this.value = value;
        emit(value);
    }

    private void emit(T value) {
        for (Consumer<T> listener : listeners) {
            listener.accept(value);
        }
    }

    private static void requireEventDispatchThread() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Listeners must be changed on the EDT");
        }
    }
}
