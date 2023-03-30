package org.flux.store.main;

import org.apache.commons.lang3.builder.DiffResult;
import org.flux.store.api.Action;
import org.flux.store.api.Reducer;
import org.flux.store.api.State;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Store<T extends State> {

    private Reducer<T> reducer;

    private T state;

    public List<Consumer<T>> listeners = new ArrayList<>();

    public Store(T initialState, Reducer<T> reducer) {
        this.state = initialState;
        this.reducer = reducer;
    }

    public void subscribe(Consumer<T> fn) {
        this.listeners.add(fn);
    }

    public void dispatch(Action action) {
        T newState = reducer.reduce(action, state);
        DiffResult diffResult = newState.diff(state);
        boolean isChanged = diffResult.getNumberOfDiffs() > 0;
        if(isChanged) {
            this.state = newState;
            this.listeners.forEach(l -> l.accept(this.state));
        }
    }

    public T getState() {
        return this.state;
    }
}
